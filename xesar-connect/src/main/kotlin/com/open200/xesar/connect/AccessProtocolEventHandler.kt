package com.open200.xesar.connect

import com.open200.xesar.connect.messages.query.AccessProtocolEvent

/** Functional interface for handling MQTT access protocol events. */
fun interface AccessProtocolEventHandler {
    fun handle(accessProtocolEvent: AccessProtocolEvent)
}
