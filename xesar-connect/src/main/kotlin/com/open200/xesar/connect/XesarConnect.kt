package com.open200.xesar.connect

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.exception.ConnectionFailedException
import com.open200.xesar.connect.exception.LoggedOutException
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.exception.UnauthorizedLoginAttemptException
import com.open200.xesar.connect.filters.CommandIdFilter
import com.open200.xesar.connect.filters.MessageFilter
import com.open200.xesar.connect.filters.QueryIdFilter
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.messages.session.*
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
    private fun buildRequestConfig(): RequestConfig {
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

    override fun close() {
        if (connectionChannel.isClosedForSend) {
            return
        }
        if (config.logoutOnClose == true && token.isNotEmpty()) {
            runBlocking { launch { logoutAsync().await() } }
        }
        client.close()
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
        val commandId = config.requestIdGenerator.generateId()

        try {
            withTimeout(requestConfig.timeout) {
                val successListener =
                    on(CommandIdFilter(commandId)) {
                        try {
                            val loggedIn = decodeSession<LoggedIn>(it.message)
                            token.complete(loggedIn.event.token)
                        } catch (e: Exception) {
                            token.completeExceptionally(
                                ConnectionFailedException("Login failed", e))
                        }
                    }
                val unauthorizedListener =
                    on(TopicFilter(Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)) {
                        val loggedIn = decodeUnauthorizedLoginAttempt(it.message)
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
                            Topics.Request.LOGIN,
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
                        val loggedOut = decodeLogout(it.message)
                        val tokenOut = loggedOut.event.token

                        if (tokenOut == requestConfig.token) {
                            token.complete(tokenOut)
                        }
                    } catch (e: Exception) {
                        token.completeExceptionally(LoggedOutException())
                    }
                }

                client
                    .publishAsync(Topics.Request.LOGOUT, encodeCommand(Logout(requestConfig.token)))
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

    /**
     * Queries the list of persons asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of persons.
     */
    suspend fun queryPersonListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<Person>> {
        return queryListAsync(Person.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of users asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of users.
     */
    suspend fun queryUserListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<User>> {
        return queryListAsync(User.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of installation points asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of installation
     *   points.
     */
    suspend fun queryInstallationPointListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<InstallationPoint>> {
        return queryListAsync(InstallationPoint.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of authorization profiles asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of authorization
     *   profiles.
     */
    suspend fun queryAuthorizationProfilesListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<AuthorizationProfile>> {
        return queryListAsync(AuthorizationProfile.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of identification media asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of identification
     *   media.
     */
    suspend fun queryIdentificationMediumListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<IdentificationMedium>> {
        return queryListAsync(IdentificationMedium.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of calendars asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of calendars.
     */
    suspend fun queryCalendarListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<Calendar>> {
        return queryListAsync(Calendar.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of coding stations asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of coding stations.
     */
    suspend fun queryCodingStationListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<CodingStation>> {
        return queryListAsync(CodingStation.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of evva components asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of evva components.
     */
    suspend fun queryEvvaComponentListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<EvvaComponent>> {
        return queryListAsync(EvvaComponent.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of office modes asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of office modes.
     */
    suspend fun queryOfficeModeListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<OfficeMode>> {
        return queryListAsync(OfficeMode.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of time profiles asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to a response containing a list of time profiles.
     */
    suspend fun queryTimeProfileListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<TimeProfile>> {
        return queryListAsync(TimeProfile.QUERY_RESOURCE, params, requestConfig)
    }

    /**
     * Queries the list of zones asynchronously.
     *
     * @param params The query parameters (optional).
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves a response containing a list of zones.
     */
    suspend fun queryZoneListAsync(
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<Zone>> {
        return queryListAsync(Zone.QUERY_RESOURCE, params, requestConfig)
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

    private suspend inline fun <reified T : QueryListResource> queryListAsync(
        resource: String,
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<QueryList.Response<T>> {
        val deferred = CompletableDeferred<QueryList.Response<T>>()
        val requestId = config.requestIdGenerator.generateId()
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

    /**
     * Queries a person by ID asynchronously.
     *
     * @param id The ID of the person to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried person.
     */
    suspend fun queryPersonByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<Person> {
        return queryElementAsync(Person.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries an installation point by ID asynchronously.
     *
     * @param id The ID of the installation point to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried installation point.
     */
    suspend fun queryInstallationPointByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<InstallationPoint> {
        return queryElementAsync(InstallationPoint.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries an authorization profile by ID asynchronously.
     *
     * @param id The ID of the authorization profile to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried authorization profile.
     */
    suspend fun queryAuthorizationProfilesByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<AuthorizationProfile> {
        return queryElementAsync(AuthorizationProfile.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries an identification media by ID asynchronously.
     *
     * @param id The ID of the identification media to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried identification media.
     */
    suspend fun queryIdentificationMediumByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<IdentificationMedium> {
        return queryElementAsync(IdentificationMedium.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries a calendar by ID asynchronously.
     *
     * @param id The ID of the calendar to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried calendar.
     */
    suspend fun queryCalendarByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<Calendar> {
        return queryElementAsync(Calendar.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries a coding station by ID asynchronously.
     *
     * @param id The ID of the coding station to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried coding station.
     */
    suspend fun queryCodingStationByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<CodingStation> {
        return queryElementAsync(CodingStation.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries an evva component by ID asynchronously.
     *
     * @param id The ID of the evva component to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried evva component.
     */
    suspend fun queryEvvaComponentByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<EvvaComponent> {
        return queryElementAsync(EvvaComponent.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries an office mode by ID asynchronously.
     *
     * @param id The ID of the office mode to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried office mode.
     */
    suspend fun queryOfficeModeByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<OfficeMode> {
        return queryElementAsync(OfficeMode.QUERY_RESOURCE, id, requestConfig)
    }
    /**
     * Queries a time profile by ID asynchronously.
     *
     * @param id The ID of the time profile to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried time profile.
     */
    suspend fun queryTimeProfileByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<TimeProfile> {
        return queryElementAsync(TimeProfile.QUERY_RESOURCE, id, requestConfig)
    }
    /**
     * Queries a zone by ID asynchronously.
     *
     * @param id The ID of the zone to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried zone.
     */
    suspend fun queryZoneByIdAsync(
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<Zone> {
        return queryElementAsync(Zone.QUERY_RESOURCE, id, requestConfig)
    }

    /**
     * Queries an access protocol event by ID asynchronously.
     *
     * @param id The ID of the access protocol event to query.
     * @param requestConfig The request configuration (optional).
     * @return A deferred object that resolves to the queried access protocol event.
     */
    suspend fun queryIdentificationMediumByMediumIdentifierAsync(
        mediumIdentifierValue: Int,
        params: Query.Params? = null,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<IdentificationMedium?> {
        val filters =
            (params?.filters
                ?: emptyList()) +
                Query.Params.Filter(
                    field = "mediumIdentifier",
                    value = mediumIdentifierValue.toString(),
                    type = FilterType.EQ)

        val paramsMedium =
            Query.Params(
                pageLimit = params?.pageLimit,
                pageOffset = params?.pageOffset,
                language = params?.language,
                sort = params?.sort,
                filters = filters)

        val queryListMedia =
            queryListAsync<IdentificationMedium>(
                    IdentificationMedium.QUERY_RESOURCE, paramsMedium, requestConfig)
                .await()

        return when {
            queryListMedia.data.isEmpty() ->
                CompletableDeferred<IdentificationMedium?>().apply { complete(null) }
            queryListMedia.data.size > 1 ->
                CompletableDeferred<IdentificationMedium?>().apply {
                    completeExceptionally(
                        MediumListSizeException(
                            "Expected exactly one element in the list with mediumIdentifier $mediumIdentifierValue, but found ${queryListMedia.data.size} elements"))
                }
            else ->
                CompletableDeferred<IdentificationMedium?>().apply {
                    complete(queryListMedia.data.first())
                }
        }
    }
    private suspend inline fun <reified T : QueryElementResource> queryElementAsync(
        resource: String,
        id: UUID,
        requestConfig: RequestConfig = buildRequestConfig()
    ): Deferred<T> {
        val deferred = CompletableDeferred<T>()
        val requestId = config.requestIdGenerator.generateId()

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
                                Topics.Event.LOGGED_OUT))
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
