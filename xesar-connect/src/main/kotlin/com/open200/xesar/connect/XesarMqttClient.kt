package com.open200.xesar.connect

import com.open200.xesar.connect.exception.ConnectionFailedException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import mu.KotlinLogging
import org.eclipse.paho.client.mqttv3.*
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence

private val log = KotlinLogging.logger {}

/**
 * Low-level MQTT wrapper around the used Paho MQTT Library
 *
 * @param client The MQTT asynchronous client.
 */
class XesarMqttClient(private val client: MqttAsyncClient) : IXesarMqttClient {

    override var onMessage: (String, ByteArray) -> Unit = { _, _ -> log.debug("onMessage") }
    override var onDisconnect: (ConnectionFailedException?) -> Unit = { log.debug("onDisconnect") }

    init {
        client.setCallback(
            object : MqttCallback {

                override fun connectionLost(cause: Throwable?) {
                    log.error(cause) { "lost connection" }
                    val exception = ConnectionFailedException("lost connection: $cause")
                    onDisconnect(exception)
                }

                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    log.debug { "Message arrived: ${message?.payload?.decodeToString()}" }
                    onMessage(topic ?: "", message?.payload ?: ByteArray(0))
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    log.debug {
                        "Received MQTT 'delivery complete' message of topic: ${token?.topics?.joinToString(",")}"
                    }
                }
            }
        )
    }

    /**
     * Subscribes to the specified topics asynchronously.
     *
     * @param topics The array of topics to subscribe to.
     * @return A deferred object that resolves to [Unit] when the subscription is successful.
     * @throws ConnectionFailedException if the subscription operation fails due to a connection
     *   issue.
     */
    override fun subscribeAsync(topics: Array<out String>, qos: Int): Deferred<Unit> {
        try {
            val result = CompletableDeferred<Unit>()
            val qosArr = IntArray(topics.size) { qos }

            log.debug { "Subscribing to topics: ${topics.joinToString(",")} with qos $qos" }

            client.subscribe(
                topics,
                qosArr,
                null,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        result.complete(Unit)
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        if (exception != null) {
                            result.completeExceptionally(exception)
                        } else {
                            result.completeExceptionally(RuntimeException("MQTT connection failed"))
                        }
                    }
                },
            )
            return result
        } catch (e: MqttException) {
            log.error("Can't subscribe", e)
            throw ConnectionFailedException("Can't subscribe to topic", e)
        }
    }

    /**
     * Publishes a message to the specified topic asynchronously.
     *
     * @param topic The topic to publish the message to.
     * @param message The message to be published.
     * @param qos The quality of service level for the message delivery (0, 1, or 2).
     * @return A deferred object that resolves to [Unit] when the message is published.
     * @throws ConnectionFailedException if the publishing operation fails due to a connection
     *   issue.
     */
    override fun publishAsync(topic: String, message: String, qos: Int): Deferred<Unit> {
        val deferredResult = CompletableDeferred<Unit>()

        log.debug { "Publishing message to topic: $topic, qos $qos, message: $message" }

        client.publish(
            topic,
            message.encodeToByteArray(),
            qos,
            false,
            null,
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    deferredResult.complete(Unit)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    exception
                        ?.let { ConnectionFailedException("Publishing failed", it) }
                        ?.let { deferredResult.completeExceptionally(it) }
                }
            },
        )

        return deferredResult
    }

    /**
     * Unsubscribes from the specified topics.
     *
     * @param topics The topics to unsubscribe from.
     */
    override fun unsubscribe(topics: Topics) {
        log.debug { "Unsubscribing from topics: ${topics.topics.joinToString(",")}" }
        client.unsubscribe(topics.topics)
    }

    /** Disconnects from the MQTT broker. */
    override fun disconnect() {
        log.debug { "Disconnecting from MQTT broker" }
        val token = client.disconnect()

        token.actionCallback =
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    onDisconnect(null)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    if (exception != null) {
                        throw ConnectionFailedException("Disconnect failed", exception)
                    } else {
                        throw ConnectionFailedException("Disconnect failed")
                    }
                }
            }
    }

    override fun isConnected(): Boolean {
        return client.isConnected
    }

    /** Closes the MQTT client by disconnecting from the broker. */
    override fun close() {
        disconnect()
    }

    override fun getClientId(): String {
        return client.clientId
    }

    companion object {

        /**
         * Connects to the MQTT broker asynchronously and returns an instance of XesarMqttClient.
         *
         * @param config The configuration for connecting to the MQTT broker.
         * @return A deferred object that resolves to an instance of XesarMqttClient upon successful
         *   connection.
         * @throws ConnectionFailedException if the connection to the MQTT broker fails.
         */
        fun connectAsync(config: Config): Deferred<XesarMqttClient> {
            val mqttClientId = config.mqttClientId ?: MqttClient.generateClientId()
            val persistence = MemoryPersistence()

            val deferredResult = CompletableDeferred<XesarMqttClient>()

            val broker = getBrokerUri(config)
            val options = MqttConnectOptions()

            options.isCleanSession = config.mqttConnectOptions.isCleanSession
            options.connectionTimeout = config.mqttConnectOptions.connectionTimeout
            options.isAutomaticReconnect = config.mqttConnectOptions.isAutomaticReconnect
            options.maxInflight = config.mqttConnectOptions.maxInflight
            options.keepAliveInterval = config.mqttConnectOptions.keepAliveInterval

            if (config.mqttCertificates != null) {
                val sslContextCertificate =
                    SslContextCertificate(
                        config.mqttCertificates,
                        config.mqttConnectOptions.securityProtocol,
                    )
                options.socketFactory = sslContextCertificate.ssLContext.socketFactory
                options.isHttpsHostnameVerificationEnabled = false
            }

            val client = MqttAsyncClient(broker, mqttClientId, persistence)

            client.connect(
                options,
                null,
                object : IMqttActionListener {
                    override fun onSuccess(asyncActionToken: IMqttToken?) {
                        deferredResult.complete(XesarMqttClient(client))
                    }

                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                        exception
                            ?.let {
                                ConnectionFailedException("Could not connect to MQTT Instance", it)
                            }
                            ?.let { deferredResult.completeExceptionally(it) }
                    }
                },
            )

            deferredResult.invokeOnCompletion {
                if (deferredResult.isCancelled) {
                    log.error("Connection failed")
                    it?.let { ConnectionFailedException("Could not connect to MQTT Instance", it) }
                }
            }

            return deferredResult
        }

        /**
         * Gets the broker URI based on the configuration.
         *
         * @param config The configuration for connecting to the MQTT broker.
         * @return The broker URI.
         */
        private fun getBrokerUri(config: Config): String {
            return if (config.mqttCertificates != null) {
                "ssl://${config.apiProperties.hostname}:${config.apiProperties.port}"
            } else {
                "tcp://${config.apiProperties.hostname}:${config.apiProperties.port}"
            }
        }
    }
}
