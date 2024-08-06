package com.open200.xesar.connect

import com.open200.xesar.connect.exception.*
import com.open200.xesar.connect.filters.*
import com.open200.xesar.connect.messages.ApiError
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.decodeError
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.*
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import kotlin.coroutines.coroutineContext
import kotlin.math.ceil
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

/**
 * Xesar Highlevel API
 *
 * Handles the connection and the session of the Xesar API client in a stateful manner.
 */
class XesarConnect(private val client: IXesarMqttClient, val config: Config) {

    private val subscribedTopics = CopyOnWriteArraySet<String>()
    private val listeners = CopyOnWriteArrayList<Listener>()
    private val connectionChannel = Channel<ConnectionEvent>()
    private val coroutineScopeForSendCommand =
        CoroutineScope(config.dispatcherForCommandsAndCleanUp)
    private val coroutineScopeForCleanUp = CoroutineScope(config.dispatcherForCommandsAndCleanUp)
    lateinit var token: Token

    /**
     * Configuration for a request.
     *
     * @property timeout The timeout value in milliseconds (default: 5000).
     * @property token The token to be included in the request.
     */
    data class RequestConfig(val timeout: Long = 5000L)
    internal fun buildRequestConfig(): RequestConfig {
        return RequestConfig()
    }

    init {
        client.onDisconnect = { connectionChannel.trySend(ConnectionEvent.DISCONNECTED) }
        client.onMessage = { topic, message ->
            // call all listeners on the topic
            val decodedMessage = message.decodeToString()
            listeners
                .filter { it.filter.filter(topic, decodedMessage) }
                .forEach { it.messageHandler.handle(MessageHandler.Message(topic, decodedMessage)) }
        }
    }

    /**
     * Subscribes to the specified topics asynchronously.
     * 1. QoS 0 (At most once): The message is delivered once, but there is no guarantee of
     *    delivery. It is a best-effort approach, where messages may be lost or duplicated.
     * 2. QoS 1 (At least once): The message is guaranteed to be delivered at least once, but it may
     *    be delivered multiple times due to network issues or failures.
     * 3. QoS 2 (Exactly once): The message is guaranteed to be delivered exactly once. It involves
     *    a more complex handshake process between the publisher and subscriber to ensure reliable
     *    delivery.
     *
     * @param topics The topics to subscribe to.
     * @param qos The MQTT quality of service
     * @return A deferred object that resolves to [Unit] when the subscription is successful.
     */
    fun subscribeAsync(topics: Topics, qos: Int = 0): Deferred<Unit> {
        return client.subscribeAsync(topics.topics, qos).apply {
            subscribedTopics.addAll(topics.topics)
        }
    }

    /**
     * Registers a listener for incoming messages that match the specified message filter.
     *
     * @param messageFilter The filter to match incoming messages against.
     * @param onMessage The message handler that will be invoked when a matching message is
     *   received.
     * @return The created listener object.
     */
    fun on(messageFilter: MessageFilter, onMessage: MessageHandler): Listener {
        val listener = Listener(this, onMessage, messageFilter)
        addListener(listener)
        return listener
    }

    /**
     * Registers a listener for incoming messages that match the specified message filter.
     *
     * @param messageFilter The filter to match incoming messages against.
     * @param onEventHandler The event handler for an event of type `E` that will be invoked when a
     *   matching message is received.
     */
    inline fun <reified E : Event> onEvent(
        messageFilter: MessageFilter,
        onEventHandler: EventHandler<E>
    ): Listener {
        return on(messageFilter) {
            val event = decodeEvent<E>(it.message)
            onEventHandler.handle(event)
        }
    }

    /**
     * Registers a listener for incoming messages that match the specified access protocol event
     * types.
     *
     * @param eventTypes The access protocol event types to match incoming messages against.
     * @param onAccessProtocolEventHandler The access protocol event handler that will be invoked
     *   when a matching message is received.
     */
    fun onAccessProtocolEvent(
        eventTypes: List<EventType>,
        onAccessProtocolEventHandler: AccessProtocolEventHandler
    ): Listener {
        return on(TopicFilter(eventTypes.map { Topics.Event.accessProtocolEventTopic(it) })) {
            val event = AccessProtocolEvent.decode(it.message)
            onAccessProtocolEventHandler.handle(event)
        }
    }

    /**
     * Retrieves a list of currently subscribed topics.
     *
     * @return The list of subscribed topics.
     */
    fun getSubscribedTopics(): List<String> {
        return subscribedTopics.toList()
    }

    /**
     * Unsubscribes from the specified topics.
     *
     * @param topics The topics to unsubscribe from.
     */
    fun unsubscribeTopics(topics: Topics) {
        subscribedTopics.removeAll(topics.topics.toSet())
        client.unsubscribe(topics)
    }

    private fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    internal fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    /**
     * Performs an asynchronous login operation where the internal token will be set within
     * XesarConnect You can either log in with a username and a password or with a token.
     *
     * @param username The username for authentication.
     * @param password The password for authentication.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a token upon successful login.
     * @throws ConnectionFailedException if the login operation fails due to a connection issue.
     * @throws UnauthorizedLoginAttemptException if the login attempt is unauthorized.
     */
    suspend fun loginAsync(
        username: String,
        password: String,
        requestConfig: RequestConfig = buildRequestConfig(),
    ): Deferred<Token> {
        val deferred = CompletableDeferred<Token>()
        val commandId = config.uuidGenerator.generateId()

        handleStandardExceptions(deferred, "Login") {
            withTimeout(requestConfig.timeout) {
                val successListener =
                    on(CommandIdFilter(commandId)) {
                        try {
                            val loggedIn = decodeEvent<LoggedIn>(it.message)
                            deferred.complete(loggedIn.event.token)
                        } catch (e: Exception) {
                            deferred.completeExceptionally(
                                ConnectionFailedException("Login failed", e))
                        }
                    }
                val unauthorizedListener =
                    on(TopicFilter(Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)) {
                        val loggedIn = decodeEvent<UnauthorizedLoginAttempt>(it.message)
                        if (loggedIn.event.username == username) {
                            logger.warn("Login failed.")
                            deferred.completeExceptionally(
                                UnauthorizedLoginAttemptException(
                                    "Probably invalid credentials were used"))
                        }
                    }
                closeListenerOnCompletion(deferred, successListener, unauthorizedListener)
                client
                    .publishAsync(
                        Topics.Command.LOGIN,
                        encodeCommand(
                            Login(commandId = commandId, username = username, password = password)))
                    .await()

                token = deferred.await()
            }
        }

        return deferred
    }

    /**
     * Performs an asynchronous logout operation. The previously used access token will be
     * invalidated by the Xesar system and cannot be used again.
     *
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the logged out token upon successful logout.
     * @throws ConnectionFailedException if the logout operation fails due to a connection issue.
     * @throws LoggedOutException if an error occurs during the logout process.
     */
    suspend fun logoutAsync(requestConfig: RequestConfig = buildRequestConfig()): Deferred<Token> {
        val deferredToken = CompletableDeferred<Token>()

        handleStandardExceptions(deferredToken, "Logout") {
            withTimeout(requestConfig.timeout) {
                val successfulListener =
                    on(TopicFilter(Topics.Event.LOGGED_OUT)) {
                        try {
                            val loggedOut = decodeEvent<LoggedOut>(it.message)
                            val tokenOut = loggedOut.event.token

                            if (tokenOut == token) {
                                deferredToken.complete(tokenOut)
                            }
                        } catch (e: Exception) {
                            deferredToken.completeExceptionally(LoggedOutException())
                        }
                    }
                closeListenerOnCompletion(deferredToken, successfulListener)
                client.publishAsync(Topics.Command.LOGOUT, encodeCommand(Logout(token))).await()
                deferredToken.await()
            }
        }

        return deferredToken
    }

    internal suspend inline fun <reified T : QueryListResource> queryListAsync(
        resource: String,
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<T>> {
        val deferred = CompletableDeferred<QueryList.Response<T>>()
        val requestId = config.uuidGenerator.generateId()
        val query = Query(resource, requestId, token, id = null, params = params)

        handleStandardExceptions(deferred, "Query") {
            withTimeout(requestConfig.timeout) {
                val queryIdListener =
                    on(QueryIdFilter(requestId)) {
                        val decoded = decodeQueryList<T>(it.message)
                        deferred.complete(decoded.response)
                    }
                val apiErrorListener = registerDefaultApiErrorListener(requestId, deferred)
                closeListenerOnCompletion(deferred, queryIdListener, apiErrorListener)
                client.publishAsync(Topics.Query.REQUEST, encodeCommand(query)).await()
                deferred.await()
            }
        }

        return deferred
    }

    internal suspend inline fun <reified T : QueryElementResource> queryElementAsync(
        resource: String,
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<T> {
        val deferred = CompletableDeferred<T>()
        val requestId = config.uuidGenerator.generateId()

        handleStandardExceptions(deferred, "Query") {
            withTimeout(requestConfig.timeout) {
                val queryListener =
                    on(QueryIdFilter(requestId)) {
                        val decoded = decodeQueryElement<T>(it.message)
                        deferred.complete(decoded.response)
                    }
                val errorListener = registerDefaultApiErrorListener(requestId, deferred)
                closeListenerOnCompletion(deferred, queryListener, errorListener)
                client
                    .publishAsync(
                        Topics.Query.REQUEST,
                        encodeCommand(Query(resource, requestId, token, id = id, params = null)))
                    .await()
                deferred.await()
            }
        }

        return deferred
    }

    internal inline fun <reified T : QueryListResource> queryStream(
        resource: String,
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Flow<T> {
        return flow {
            val firstQueryListResponse = queryListAsync<T>(resource, params, requestConfig).await()
            firstQueryListResponse.data.forEach { emit(it) }

            val totalNumberOfElements = firstQueryListResponse.filterCount

            var pageOffset = params?.pageOffset ?: 0
            val numberOfElementsInAPage = params?.pageLimit ?: 50

            if (totalNumberOfElements <= numberOfElementsInAPage) {
                return@flow
            }

            val totalPages =
                ceil(totalNumberOfElements.toDouble() / numberOfElementsInAPage.toDouble()).toInt()

            val pageIndex = pageOffset / numberOfElementsInAPage

            // minus 1 because I already fetched data with one query
            val remainingPages = totalPages - 1 - pageIndex

            val paramsIter = params ?: Query.Params()
            for (page in 0 until remainingPages) {
                pageOffset += numberOfElementsInAPage
                val queryListResponse =
                    queryListAsync<T>(
                            resource,
                            paramsIter.copy(
                                pageOffset = pageOffset, pageLimit = numberOfElementsInAPage),
                            requestConfig)
                        .await()

                queryListResponse.data.forEach { emit(it) }
            }
        }
    }

    private fun <T> closeListenerOnCompletion(
        deferred: CompletableDeferred<T>,
        vararg listeners: Listener
    ) {
        deferred.invokeOnCompletion { listeners.forEach { it.close() } }
    }

    private fun closeListener(listeners: List<Listener>) {
        listeners.forEach { it.close() }
    }

    private suspend inline fun <T> handleStandardExceptions(
        deferred: CompletableDeferred<T>,
        messageType: String,
        block: () -> (Unit)
    ) {
        try {
            block.invoke()
        } catch (e: TimeoutCancellationException) {
            logger.error("Timeout while waiting for $messageType response", e)
            deferred.completeExceptionally(
                ConnectionFailedException("$messageType request timed out", e))
        } catch (e: Exception) {
            logger.error("Error while waiting for $messageType response")
            deferred.completeExceptionally(ConnectionFailedException("$messageType failed", e))
        }
    }

    /**
     * Sends a command and returns a [com.open200.xesar.connect.messages.SingleEventResult]
     * asynchronously. If the event is not received within the specified timeout, a
     * [RequiredEventException] or an [OptionalEventException] is thrown for the [Deferred] of the
     * event. To analyse these timeout exceptions check
     * [com.open200.xesar.connect.messages.SingleEventResult.apiErrorDeferred] for an [ApiError].
     *
     * @param topicCommand The topic to send the command.
     * @param topicEvent The topic to wait for the event.
     * @param eventRequired Whether the event is required.
     * @param command The command to send.
     * @param requestConfig The request configuration (optional).
     */
    internal suspend inline fun <reified C : Command, reified E1 : Event> sendCommandAsync(
        topicCommand: String,
        topicEvent: String,
        eventRequired: Boolean,
        command: C,
        requestConfig: RequestConfig = buildRequestConfig()
    ): SingleEventResult<E1> {
        val firstEventDeferred = CompletableDeferred<E1>()
        val apiErrorDeferred = CompletableDeferred<Optional<ApiError>>()

        val firstEventListener =
            registerCommandEventListenerAsync<C, E1>(command, topicEvent, firstEventDeferred)
        val errorListener = registerApiErrorListener<C>(command, apiErrorDeferred)
        val listener = listOf(errorListener, firstEventListener)
        val sendCommandJob =
            sendAndWaitForCommandAsync<C>(
                requestConfig,
                topicCommand,
                command,
                listOf(firstEventDeferred),
                apiErrorDeferred,
                listener)
        sendCommandJob.invokeOnCompletion { commandException ->
            if (commandException == null) {
                val eventJob =
                    waitingForEventAsync<E1>(
                        requestConfig, firstEventDeferred, eventRequired, topicEvent, listener[1])
                registerToCloseResourcesOnCancellation(eventJob, listener[1], firstEventDeferred)

                val apiErrorJob =
                    waitingForApiErrorAsync(requestConfig, apiErrorDeferred, listener[0])
                registerToCloseResourcesOnCancellation(apiErrorJob, listener[0], apiErrorDeferred)
            }
        }
        return SingleEventResult(firstEventDeferred, apiErrorDeferred)
    }

    private fun <T> registerToCloseResourcesOnCancellation(
        job: Job,
        listener: Listener,
        deferred: CompletableDeferred<T>
    ) {
        job.invokeOnCompletion {
            when (it) {
                is CancellationException -> {
                    logger.debug { "cancelled job" }
                    listener.close()
                    deferred.completeExceptionally(it)
                }
            }
        }
    }

    /**
     * Sends a command and returns a [Triple] including the two [Deferred] for two events and one
     * [Deferred] for the [ApiError] asynchronously. If the events are not received within the
     * specified timeout, a [RequiredEventException] or an [OptionalEventException] is thrown for
     * the [Deferred] of the events respectively. To analyse these timeout exceptions check the
     * [Deferred] for the [ApiError].
     *
     * @param topicCommand The topic to send the command.
     * @param firstTopicEvent The topic to wait for the first event.
     * @param firstEventRequired Whether the first event is required.
     * @param secondTopicEvent The topic to wait for the second event.
     * @param secondEventRequired Whether the second event is required.
     * @param command The command to send.
     * @param requestConfig The request configuration (optional).
     */
    internal suspend inline fun <
        reified C : Command, reified E1 : Event, reified E2 : Event> sendCommandAsync(
        topicCommand: String,
        firstTopicEvent: String,
        firstEventRequired: Boolean,
        secondTopicEvent: String,
        secondEventRequired: Boolean,
        command: C,
        requestConfig: RequestConfig = buildRequestConfig(),
    ): Triple<Deferred<E1>, Deferred<E2>, Deferred<Optional<ApiError>>> {

        val firstEventDeferred = CompletableDeferred<E1>()
        val secondEventDeferred = CompletableDeferred<E2>()
        val apiErrorDeferred = CompletableDeferred<Optional<ApiError>>()

        val firstEventListener =
            registerCommandEventListenerAsync<C, E1>(command, firstTopicEvent, firstEventDeferred)
        val secondEventListener =
            registerCommandEventListenerAsync<C, E2>(command, secondTopicEvent, secondEventDeferred)
        val errorListener = registerApiErrorListener<C>(command, apiErrorDeferred)

        val listener = listOf(errorListener, firstEventListener, secondEventListener)

        val sendCommandJob =
            sendAndWaitForCommandAsync<C>(
                requestConfig,
                topicCommand,
                command,
                listOf(firstEventDeferred, secondEventDeferred),
                apiErrorDeferred,
                listener)

        sendCommandJob.invokeOnCompletion { commandException ->
            if (commandException == null) {
                logger.debug { "start waiting for events" }

                val firstEventJob =
                    waitingForEventAsync<E1>(
                        requestConfig,
                        firstEventDeferred,
                        firstEventRequired,
                        firstTopicEvent,
                        listener[1])
                registerToCloseResourcesOnCancellation(
                    firstEventJob, listener[1], firstEventDeferred)

                val secondEventJob =
                    waitingForEventAsync<E2>(
                        requestConfig,
                        secondEventDeferred,
                        secondEventRequired,
                        secondTopicEvent,
                        listener[2])
                registerToCloseResourcesOnCancellation(
                    secondEventJob, listener[2], secondEventDeferred)

                val apiErrorJob =
                    waitingForApiErrorAsync(requestConfig, apiErrorDeferred, listener[0])
                registerToCloseResourcesOnCancellation(apiErrorJob, listener[0], apiErrorDeferred)
            }
        }

        return Triple(firstEventDeferred, secondEventDeferred, apiErrorDeferred)
    }

    /**
     * Sends a command and returns a [Pair] including a [Triple] with three [Deferred] for three
     * events and one [Deferred] for the [ApiError] asynchronously. If the events are not received
     * within the specified timeout, a [RequiredEventException] or an [OptionalEventException] is
     * thrown for the [Deferred] of the events respectively. To analyse these timeout exceptions
     * check the [Deferred] for the [ApiError].
     *
     * @param topicCommand The topic to send the command.
     * @param firstTopicEvent The topic to wait for the first event.
     * @param firstEventRequired Whether the first event is required.
     * @param secondTopicEvent The topic to wait for the second event.
     * @param secondEventRequired Whether the second event is required.
     * @param thirdTopicEvent The topic to wait for the third event.
     * @param thirdEventRequired Whether the third event is required.
     * @param command The command to send.
     * @param requestConfig The request configuration (optional).
     */
    internal suspend inline fun <
        reified C : Command,
        reified E1 : Event,
        reified E2 : Event,
        reified E3 : Event> sendCommandAsync(
        topicCommand: String,
        firstTopicEvent: String,
        firstEventRequired: Boolean,
        secondTopicEvent: String,
        secondEventRequired: Boolean,
        thirdTopicEvent: String,
        thirdEventRequired: Boolean,
        command: C,
        requestConfig: RequestConfig = buildRequestConfig(),
    ): Pair<Triple<Deferred<E1>, Deferred<E2>, Deferred<E3>>, Deferred<Optional<ApiError>>> {

        val firstEventDeferred = CompletableDeferred<E1>()
        val secondEventDeferred = CompletableDeferred<E2>()
        val thirdEventDeferred = CompletableDeferred<E3>()
        val apiErrorDeferred = CompletableDeferred<Optional<ApiError>>()

        val firstEventListenerDeferred =
            registerCommandEventListenerAsync<C, E1>(command, firstTopicEvent, firstEventDeferred)
        val secondEventListenerDeferred =
            registerCommandEventListenerAsync<C, E2>(command, secondTopicEvent, secondEventDeferred)
        val thirdEventListenerDeferred =
            registerCommandEventListenerAsync<C, E3>(command, thirdTopicEvent, thirdEventDeferred)
        val errorListener = registerApiErrorListener<C>(command, apiErrorDeferred)

        val listener =
            listOf(
                errorListener,
                firstEventListenerDeferred,
                secondEventListenerDeferred,
                thirdEventListenerDeferred)

        val sendCommandJob =
            sendAndWaitForCommandAsync<C>(
                requestConfig,
                topicCommand,
                command,
                listOf(firstEventDeferred, secondEventDeferred, thirdEventDeferred),
                apiErrorDeferred,
                listener)

        sendCommandJob.invokeOnCompletion { commandException ->
            if (commandException == null) {
                logger.debug { "start waiting for events" }
                val firstEventJob =
                    waitingForEventAsync<E1>(
                        requestConfig,
                        firstEventDeferred,
                        firstEventRequired,
                        firstTopicEvent,
                        listener[1])

                registerToCloseResourcesOnCancellation(
                    firstEventJob, listener[1], firstEventDeferred)

                val secondEventJob =
                    waitingForEventAsync<E2>(
                        requestConfig,
                        secondEventDeferred,
                        secondEventRequired,
                        secondTopicEvent,
                        listener[2])

                registerToCloseResourcesOnCancellation(
                    secondEventJob, listener[2], secondEventDeferred)

                val thirdEventJob =
                    waitingForEventAsync<E3>(
                        requestConfig,
                        thirdEventDeferred,
                        thirdEventRequired,
                        thirdTopicEvent,
                        listener[3])

                registerToCloseResourcesOnCancellation(
                    thirdEventJob, listener[3], thirdEventDeferred)

                val apiErrorJob =
                    waitingForApiErrorAsync(requestConfig, apiErrorDeferred, listener[0])

                registerToCloseResourcesOnCancellation(apiErrorJob, listener[0], apiErrorDeferred)
            }
        }
        return Pair(
            Triple(firstEventDeferred, secondEventDeferred, thirdEventDeferred), apiErrorDeferred)
    }

    private suspend inline fun <reified C : Command> sendAndWaitForCommandAsync(
        requestConfig: RequestConfig,
        topicCommand: String,
        command: C,
        listOfEventDeferred: List<CompletableDeferred<out Event>>,
        apiErrorDeferred: CompletableDeferred<Optional<ApiError>>,
        listeners: List<Listener>
    ): Job {
        var publishDeferred = CompletableDeferred<Unit>()
        return coroutineScopeForSendCommand.launch {
            try {
                withTimeout(requestConfig.timeout) {
                    logger.debug { "send commmand now" }
                    publishDeferred =
                        client.publishAsync(topicCommand, encodeCommand(command))
                            as CompletableDeferred<Unit>
                    publishDeferred.await()
                }
            } catch (e: TimeoutCancellationException) {
                logger.error { "timeout while sending the command" }
                completeWithSpecificException(
                    ConnectionFailedException("Command Response timed out", e),
                    listOf(publishDeferred, *listOfEventDeferred.toTypedArray(), apiErrorDeferred))
                closeListener(listeners)
            } catch (e: Exception) {
                logger.error { "error while sending the command" }
                completeWithSpecificException(
                    ConnectionFailedException("Command request was invalid", e),
                    listOf(publishDeferred, *listOfEventDeferred.toTypedArray(), apiErrorDeferred))
                closeListener(listeners)
            }
        }
    }

    private fun waitingForApiErrorAsync(
        requestConfig: RequestConfig,
        apiErrorDeferred: CompletableDeferred<Optional<ApiError>>,
        listener: Listener
    ): Job =
        coroutineScopeForSendCommand.launch {
            logger.debug { "waiting for api error" }
            try {
                withTimeout(requestConfig.timeout) { apiErrorDeferred.await() }
            } catch (e: TimeoutCancellationException) {
                logger.debug { "timeout while waiting for api error" }
                apiErrorDeferred.complete(Optional.empty())
            } catch (e: Exception) {
                logger.error { "exception while waiting for api error ${e.message.orEmpty()}" }
                apiErrorDeferred.completeExceptionally(e)
            } finally {
                listener.close()
                logger.debug { "listener closed for api error" }
            }
        }

    private inline fun <reified E : Event> waitingForEventAsync(
        requestConfig: RequestConfig,
        eventDeferred: CompletableDeferred<E>,
        eventRequired: Boolean,
        topicEvent: String,
        listener: Listener
    ): Job =
        coroutineScopeForSendCommand.launch {
            logger.debug { "waiting for event $topicEvent" }
            try {
                withTimeout(requestConfig.timeout) { eventDeferred.await() }
            } catch (e: TimeoutCancellationException) {
                val logMessage = "timeout while waiting for event $topicEvent"
                when (eventRequired) {
                    true -> logger.error { logMessage }
                    false -> logger.debug { logMessage }
                }
                completeWithSpecificException(
                    getTypeOfExceptionDependingOnEventRequired(eventRequired, topicEvent, e),
                    listOf(eventDeferred))
            } catch (e: Exception) {
                logger.error { "exception while waiting for event $topicEvent" }
                completeWithSpecificException(e, listOf(eventDeferred))
            } finally {
                listener.close()
                logger.debug { "listener closed for $topicEvent" }
            }
        }

    private inline fun <reified C : Command> registerApiErrorListener(
        command: C,
        apiErrorDeferred: CompletableDeferred<Optional<ApiError>>
    ): Listener =
        on(ApiErrorFilter(command.commandId, Topics.Event.error(config.apiProperties.userId))) {
            try {
                logger.debug { "decode api error message" }
                val apiError = decodeError(it.message)
                apiErrorDeferred.complete(Optional.of(apiError))
            } catch (e: Exception) {
                logger.error { "exception while waiting for api error: ${e.message}" }
                apiErrorDeferred.completeExceptionally(e)
            }
        }

    private inline fun <reified C : Command, reified E : Event> registerCommandEventListenerAsync(
        command: C,
        topicEvent: String,
        eventDeferred: CompletableDeferred<E>
    ): Listener =
        on(EventAndCommandIdFilter(command.commandId, topicEvent)) {
            try {
                logger.debug { "decode $topicEvent message" }
                val apiEvent = decodeEvent<E>(it.message)
                eventDeferred.complete(apiEvent.event)
            } catch (e: Exception) {
                eventDeferred.completeExceptionally(ParsingException())
            }
        }

    private fun <T> registerDefaultApiErrorListener(
        id: UUID,
        deferred: CompletableDeferred<T>
    ): Listener =
        on(ApiErrorFilter(id, Topics.Event.error(config.apiProperties.userId))) {
            try {
                val apiError = decodeError(it.message)
                deferred.completeExceptionally(
                    HttpErrorException(
                        "HTTP error code: ${apiError.error} ${apiError.reason.orEmpty()}",
                        apiError.error))
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }
        }

    private fun completeWithSpecificException(
        e: Exception,
        deferreds: List<CompletableDeferred<*>>
    ) {
        deferreds.forEach { it.completeExceptionally(e) }
    }

    private fun getTypeOfExceptionDependingOnEventRequired(
        eventRequired: Boolean,
        topic: String,
        e: Throwable
    ): XesarApiException {
        return if (eventRequired) {
            RequiredEventException("Required $topic event not received within timeout", e)
        } else {
            OptionalEventException("Optional $topic event not received within timeout", e)
        }
    }

    /**
     * Suspends the execution of the current coroutine until a disconnection event occurs in the
     * specified channel.
     *
     * This function monitors the `connectionChannel` for the `ConnectionEvent.DISCONNECTED` event.
     * It suspends the coroutine until the disconnection event is received, at which point it sets
     * `connected` to `false` and closes the `connectionChannel`.
     *
     * @throws CancellationException if the coroutine is canceled while waiting.
     */
    suspend fun delay() {
        var connected = true

        while (connected) {
            // Wait for a disconnection event in the channel.
            if (connectionChannel.receive() == ConnectionEvent.DISCONNECTED) {
                connected = false
                connectionChannel.close()
            }
        }
    }

    /** Breaks out of the delay function. */
    suspend fun breakOutOfDelay() {
        connectionChannel.trySend(ConnectionEvent.DISCONNECTED)
    }

    /**
     * Invokes [cleanUp] when the coroutine which called the [XesarConnect.connectAndLoginAsync] is
     * completed.
     */
    private suspend fun invokeCleanUpOnCompletion() {
        coroutineContext[Job]?.let { jobCallingXesarConnect ->
            jobCallingXesarConnect.invokeOnCompletion {
                val job = coroutineScopeForCleanUp.launch { cleanUp() }
                job.invokeOnCompletion {
                    it?.let { logger.error { "Clean up failed" } }
                        ?: run { logger.info { "Clean up completed" } }
                    coroutineScopeForCleanUp.cancel(CancellationException("program was closed"))
                }
            }
        }
    }

    /**
     * This clean up is internally handled within [XesarConnect.connectAndLoginAsync]
     *
     * This clean up function does the following:
     * 1. Regardless of whether the connection channel is closed or not cancel/clean up all
     *    underlying coroutines (close listener, complete deferred) which are still active.
     * 2. If the [Config.logoutOnClose] configuration is set to true and a token was used for login,
     *    perform a logout operation and close the mqtt client to disconnect from the MQTT broker.
     * 3. Cancel and clean up all underlying coroutines (close listener, complete deferred).
     */
    suspend fun cleanUp() {
        logger.debug { "clean up" }
        if (!connectionChannel.isClosedForSend) {
            // channel (connection to mqtt broker) still open so logout if needed
            if (config.logoutOnClose && token.isNotEmpty()) {
                val job =
                    coroutineScopeForSendCommand.launch {
                        logoutAsync().await()
                        logger.debug { "logout completed" }
                    }
                job.join()
            }
            if (client.isConnected()) {
                client.close()
            }
        }
        logger.debug { "cancel all underlying coroutines which are still active" }
        coroutineScopeForSendCommand.cancel(CancellationException("clean up function was executed"))
    }

    internal suspend inline fun <reified T : QueryListResource> handleQueryListFunction(
        queryListAsyncFunction: () -> Deferred<QueryList.Response<T>>
    ): QueryList.Response<T> {
        try {
            return queryListAsyncFunction.invoke().await()
        } catch (e: HttpErrorException) {
            if (e.httpErrorCode == 404) {
                return QueryList.Response(emptyList(), 0, 0)
            }
            throw e
        }
    }

    internal suspend inline fun <reified T : QueryElementResource> handleQueryElementFunction(
        queryElementAsyncFunction: () -> Deferred<T>
    ): T? {
        try {
            return queryElementAsyncFunction.invoke().await()
        } catch (e: HttpErrorException) {
            if (e.httpErrorCode == 404) {
                return null
            }
            throw e
        }
    }

    companion object {
        /**
         * Asynchronously connects to the Xesar API, performs a login operation, and returns an
         * instance of the XesarApi. Internally subscribes to the following topics:
         * [Topics.Event.loggedIn],[Topics.Event.LOGGED_OUT],[Topics.Event.error],[Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT],
         *
         * @param config The configuration for connecting to the Xesar system.
         * @param userCredentials Optional user credentials for authentication, including a username
         *   and password.
         * @param closeUponCoroutineCompletion True indicates that [cleanUp] is invoked when the
         *   coroutine that called [XesarConnect.connectAndLoginAsync] completes. Use False when you
         *   want to manually execute [cleanUp].
         * @return A [Deferred] object representing the result of the connection and login process.
         *   The [Deferred] result is of type [XesarConnect].
         * @throws Exception if an error occurs during the connection or login process.
         */
        suspend fun connectAndLoginAsync(
            config: Config,
            userCredentials: UserCredentials? = null,
            closeUponCoroutineCompletion: Boolean = true
        ): Deferred<XesarConnect> {
            val deferred = CompletableDeferred<XesarConnect>()

            try {
                val client = XesarMqttClient.connectAsync(config).await()
                val api = XesarConnect(client, config)

                if (closeUponCoroutineCompletion) {
                    api.invokeCleanUpOnCompletion()
                }

                api.subscribeAsync(
                        Topics(
                            Topics.Event.loggedIn(config.apiProperties.userId),
                            Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                            Topics.Event.LOGGED_OUT,
                            Topics.Event.error(config.apiProperties.userId)))
                    .await()
                if (userCredentials != null) {
                    api.loginAsync(userCredentials.username, userCredentials.password).await()
                    deferred.complete(api)
                } else if (config.apiProperties.token != null) {
                    api.token = config.apiProperties.token
                    deferred.complete(api)
                } else {
                    deferred.completeExceptionally(
                        ConnectionFailedException("Neither token nor credentials provided"))
                }
            } catch (e: Exception) {
                deferred.completeExceptionally(e)
            }

            return deferred
        }
    }
}
