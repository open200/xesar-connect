package com.open200.xesar.connect

/** Functional interface for handling MQTT messages. */
fun interface MessageHandler {

    /**
     * Represents an MQTT message.
     *
     * @property topic The topic of the message.
     * @property message The content of the message.
     */
    data class Message(val topic: String, val message: String)

    /**
     * Handles the MQTT message.
     *
     * @param message The MQTT message to handle.
     */
    fun handle(message: Message)
}
