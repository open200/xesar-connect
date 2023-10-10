package com.open200.xesar.connect.filters

/** Functional interface for filtering messages based on a topic and message payload. */
fun interface MessageFilter {
    /**
     * Filters the given topic and message.
     *
     * @param topic The topic of the message.
     * @param message The message payload.
     * @return `true` to accept the message, `false` to reject it.
     */
    fun filter(topic: String, message: String): Boolean
}
