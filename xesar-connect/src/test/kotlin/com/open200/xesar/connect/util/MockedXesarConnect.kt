package com.open200.xesar.connect.util

import com.open200.xesar.connect.Config
import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.Event
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.assertions.fail
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

/*
 * Test harness which creates a XesarConnect instance without a MQTT broker.
 * - the MqttAsyncClient is mocked and published messages are recorded instead of being sent
 * - the backend is simulated with responders which are called when a message is published on a topic
 * - incoming messages from the broker are simulated by calling onMessage of the XesarMqttClient
 * - login is not needed, the token is initialized directly
 *
 * Usage:
 *   val harness = MockedXesarConnect(this)
 *   harness.respondTo(Topics.Command.LOCK_MEDIUM) { emitEvent(Topics.Event.MEDIUM_LOCKED, event) }
 *   val result = harness.api.lockMediumAsync(mediumId).await()
 *   harness.publishedPayload(Topics.Command.LOCK_MEDIUM).shouldBeEqual("...")
 */
class MockedXesarConnect(
    scope: TestScope,
    val commandId: UUID = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
) {
    val userId: UUID = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
    val token = TOKEN

    val client: XesarMqttClient =
        spyk(XesarMqttClient(mockk<MqttAsyncClient> { every { setCallback(any()) } just Runs }))

    private val published = CopyOnWriteArrayList<Pair<String, String>>()
    private val responders = ConcurrentHashMap<String, MockedXesarConnect.(String) -> Unit>()

    val api: XesarConnect

    init {
        every { client.publishAsync(any(), any(), any()) } answers
            {
                val topic = firstArg<String>()
                val payload = secondArg<String>()
                published.add(topic to payload)
                responders[topic]?.invoke(this@MockedXesarConnect, payload)
                CompletableDeferred(Unit)
            }

        val config =
            mockk<Config> {
                every { dispatcherForCommandsAndCleanUp } returns
                    StandardTestDispatcher(scope.testScheduler)
                every { uuidGenerator.generateId() } returns commandId
                every { apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = userId,
                        token = token,
                    )
            }

        api = XesarConnect(client, config).also { it.token = token }
    }

    /**
     * Simulates the backend: [responder] is called with the payload whenever a message is published
     * on [topic]. Don't assert inside the responder, use [publishedPayload] after the call instead.
     */
    fun respondTo(topic: String, responder: MockedXesarConnect.(payload: String) -> Unit) {
        responders[topic] = responder
    }

    /** Simulates an event for [commandId] received from the broker on [topic]. */
    inline fun <reified E : Event> emitEvent(topic: String, event: E) {
        emitMessage(topic, encodeEvent(ApiEvent(commandId, event)))
    }

    /** Simulates an encoded query result received from the broker. */
    fun emitQueryResult(payload: String) {
        emitMessage(Topics.Query.result(userId), payload)
    }

    /** Simulates a raw message received from the broker on [topic]. */
    fun emitMessage(topic: String, payload: String) {
        client.onMessage(topic, payload.encodeToByteArray())
    }

    /** Returns the last payload published on [topic] or fails if nothing was published there. */
    fun publishedPayload(topic: String): String =
        published.lastOrNull { it.first == topic }?.second
            ?: fail(
                "nothing published on topic $topic, published topics: ${published.map { it.first }}"
            )

    companion object {
        const val TOKEN =
            "JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx"
    }
}
