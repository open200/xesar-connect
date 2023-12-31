package com.open200.xesar.connect

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.exception.*
import com.open200.xesar.connect.filters.CommandIdFilter
import com.open200.xesar.connect.filters.MessageFilter
import com.open200.xesar.connect.filters.QueryIdFilter
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.event.ErrorEvent
import com.open200.xesar.connect.messages.query.*
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CopyOnWriteArraySet
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import mu.KotlinLogging

val logger = KotlinLogging.logger {}

/**
 * Xesar Highlevel API
 *
 * Handles the connection and the session of the Xesar API client in a stateful manner.
 */
class XesarConnect(private val client: IXesarMqttClient, val config: Config) : AutoCloseable {

    private val subscribedTopics = CopyOnWriteArraySet<String>()
    private val listeners = CopyOnWriteArrayList<Listener>()
    private val connectionChannel = Channel<ConnectionEvent>()

    var token: Token = ""

    /**
     * Configuration for a request.
     *
     * @property timeout The timeout value in milliseconds (default: 5000).
     * @property token The token to be included in the request.
     */
    data class RequestConfig(val timeout: Long = 5000L, val token: Token) {}
    internal fun buildRequestConfig(): RequestConfig {
        return RequestConfig(token = token)
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

    internal fun addListener(listener: Listener) {
        listeners.add(listener)
    }

    internal fun removeListener(listener: Listener) {
        listeners.remove(listener)
    }

    /**
     * Performs an asynchronous login operation.
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
        val token = CompletableDeferred<Token>()
        val commandId = config.uuidGenerator.generateId()

        try {
            withTimeout(requestConfig.timeout) {
                val successListener =
                    on(CommandIdFilter(commandId)) {
                        try {
                            val loggedIn = decodeEvent<LoggedIn>(it.message)
                            token.complete(loggedIn.event.token)
                        } catch (e: Exception) {
                            token.completeExceptionally(
                                ConnectionFailedException("Login failed", e))
                        }
                    }
                val unauthorizedListener =
                    on(TopicFilter(Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)) {
                        val loggedIn = decodeEvent<UnauthorizedLoginAttempt>(it.message)
                        if (loggedIn.event.username == username) {
                            logger.warn("Login failed.")
                            token.completeExceptionally(
                                UnauthorizedLoginAttemptException(
                                    "Probably invalid credentials were used"))
                        }
                    }
                try {
                    client
                        .publishAsync(
                            Topics.Command.LOGIN,
                            encodeCommand(
                                Login(
                                    commandId = commandId,
                                    username = username,
                                    password = password)))
                        .await()
                    token.await()
                } finally {
                    successListener.close()
                    unauthorizedListener.close()
                }
            }
        } catch (e: TimeoutCancellationException) {
            logger.error("Timeout while waiting for Login response", e)
            token.completeExceptionally(ConnectionFailedException("Login response timed out", e))
        } catch (e: Exception) {
            logger.error("Error while waiting for Login response", e)
            token.completeExceptionally(ConnectionFailedException("Login failed", e))
        }

        return token
    }

    /**
     * Performs an asynchronous logout operation.
     *
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the logged out token upon successful logout.
     * @throws ConnectionFailedException if the logout operation fails due to a connection issue.
     * @throws LoggedOutException if an error occurs during the logout process.
     */
    suspend fun logoutAsync(requestConfig: RequestConfig = buildRequestConfig()): Deferred<Token> {
        val token = CompletableDeferred<Token>()

        try {
            withTimeout(requestConfig.timeout) {
                on(TopicFilter(Topics.Event.LOGGED_OUT)) {
                    try {
                        val loggedOut = decodeEvent<LoggedOut>(it.message)
                        val tokenOut = loggedOut.event.token

                        if (tokenOut == requestConfig.token) {
                            token.complete(tokenOut)
                        }
                    } catch (e: Exception) {
                        token.completeExceptionally(LoggedOutException())
                    }
                }

                client
                    .publishAsync(Topics.Command.LOGOUT, encodeCommand(Logout(requestConfig.token)))
                    .await()
                token.await()
            }
        } catch (e: TimeoutCancellationException) {
            logger.error("Timeout while waiting for Logout response", e)
            token.completeExceptionally(ConnectionFailedException("Logout response timed out", e))
        } catch (e: Exception) {
            logger.error("Error while waiting for Logout response", e)
            token.completeExceptionally(ConnectionFailedException("Logout failed", e))
        }

        return token
    }

    /**
     * Queries the list of access protocols asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of access protocols.
     */
    suspend fun queryAccessProtocolEventListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<AccessProtocolEvent>> {
        return queryListAsync(AccessProtocolEvent.QUERY_RESOURCE, params, requestConfig)
    }

    internal suspend inline fun <reified T : QueryListResource> queryListAsync(
        resource: String,
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<T>> {
        val deferred = CompletableDeferred<QueryList.Response<T>>()
        val requestId = config.uuidGenerator.generateId()
        val query = Query(resource, requestId, requestConfig.token, id = null, params = params)

        try {
            withTimeout(requestConfig.timeout) {
                on(QueryIdFilter(requestId)) {
                        val decoded = decodeQueryList<T>(it.message)
                        deferred.complete(decoded.response)
                    }
                    .use {
                        client.publishAsync(Topics.Query.REQUEST, encodeCommand(query)).await()
                        deferred.await()
                    }
            }
        } catch (e: TimeoutCancellationException) {
            logger.error("Timeout while waiting for Query response", e)
            deferred.completeExceptionally(ConnectionFailedException("Query Response timed out", e))
        } catch (e: Exception) {
            logger.error("Error while waiting for Query response", e)
            deferred.completeExceptionally(ConnectionFailedException("Query response failed", e))
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

        try {
            withTimeout(requestConfig.timeout) {
                on(QueryIdFilter(requestId)) {
                        val decoded = decodeQueryElement<T>(it.message)
                        deferred.complete(decoded.response)
                    }
                    .use {
                        client
                            .publishAsync(
                                Topics.Query.REQUEST,
                                encodeCommand(
                                    Query(
                                        resource,
                                        requestId,
                                        requestConfig.token,
                                        id = id,
                                        params = null)))
                            .await()
                        deferred.await()
                    }
            }
        } catch (e: TimeoutCancellationException) {
            logger.error("Timeout while waiting for Query response", e)
            deferred.completeExceptionally(
                ConnectionFailedException("Query request was invalid", e))
        } catch (e: Exception) {
            logger.error("Error while waiting for Query response", e)
            deferred.completeExceptionally(
                ConnectionFailedException(
                    "Query request was invalid and Timeout was not catched", e))
        }

        return deferred
    }

    internal suspend inline fun <reified K : Command, reified T : Event> sendCommand(
        topic: String,
        cmd: K,
        requestConfig: RequestConfig
    ): Deferred<T> {
        val deferred = CompletableDeferred<T>()
        val commandId = config.uuidGenerator.generateId()

        try {
            withTimeout(requestConfig.timeout) {
                on(CommandIdFilter(commandId)) {
                    try {
                        val apiEvent = decodeEvent<T>(it.message)
                        deferred.complete(apiEvent.event)
                    } catch (e: Exception) {
                        deferred.completeExceptionally(ParsingException())
                    }
                }

                on(TopicFilter(Topics.Event.error(config.apiProperties.userId))) {
                    try {
                        val apiEvent = decodeEvent<ErrorEvent>(it.message)
                        deferred.completeExceptionally(
                            HttpErrorException("${apiEvent.event.error}"))
                    } catch (e: Exception) {
                        deferred.completeExceptionally(ParsingException())
                    }
                }

                client.publishAsync(topic, encodeCommand(cmd)).await()
            }
        } catch (e: TimeoutCancellationException) {
            logger.error("Timeout while waiting for Command response", e)
            deferred.completeExceptionally(
                ConnectionFailedException("Command Response timed out", e))
        }

        return deferred
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
    suspend fun delayUntilClose() {
        var connected = true

        while (connected) {
            // Wait for a disconnection event in the channel.
            if (connectionChannel.receive() == ConnectionEvent.DISCONNECTED) {
                connected = false
                connectionChannel.close()
            }
        }
    }

    override fun close() {
        if (connectionChannel.isClosedForSend) {
            return
        }
        if (config.logoutOnClose == true && token.isNotEmpty()) {
            runBlocking { launch { logoutAsync().await() } }
        }
        client.close()
    }

    companion object {
        /**
         * Asynchronously connects to the Xesar API, performs a login operation, and returns an
         * instance of the XesarApi.
         *
         * @param config The configuration for connecting to the Xesar system.
         * @param userCredentials Optional user credentials for authentication, including a username
         *   and password.
         * @return A [Deferred] object representing the result of the connection and login process.
         *   The [Deferred] result is of type [XesarConnect].
         * @throws Exception if an error occurs during the connection or login process.
         */
        suspend fun connectAndLoginAsync(
            config: Config,
            userCredentials: UserCredentials? = null,
        ): Deferred<XesarConnect> {
            val deferred = CompletableDeferred<XesarConnect>()

            try {
                val updatedConfig =
                    if (config.logoutOnClose == null) {
                        config.copy(logoutOnClose = true)
                    } else {
                        config
                    }
                val client = XesarMqttClient.connectAsync(updatedConfig).await()
                val api = XesarConnect(client, config)

                if (userCredentials != null) {
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.loggedIn(config.apiProperties.userId),
                                Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                                Topics.Event.LOGGED_OUT,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()
                    api.token =
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
