package com.open200.xesar.connect

import com.open200.xesar.connect.exception.ConnectionFailedException
import kotlinx.coroutines.Deferred

// todo check Kotlin naming guides
// todo add documentation
interface IXesarMqttClient : AutoCloseable {
    /** Callback for handling incoming messages on topics the client is subscribed to. */
    var onMessage: (topic: String, payload: ByteArray) -> Unit

    /** Callback for handling planned and unplanned disconnects. */
    var onDisconnect: (ConnectionFailedException?) -> Unit

    fun subscribeAsync(topics: Array<out String>, qos: Int = 0): Deferred<Unit>
    fun publishAsync(topic: String, message: String, qos: Int = 0): Deferred<Unit>
    fun unsubscribe(topics: Topics)
    fun disconnect()
}
