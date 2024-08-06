package com.open200.xesar.connect

import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.Event

/** Functional interface for handling MQTT events of type `E`. */
fun interface EventHandler<E : Event> {
    fun handle(apiEvent: ApiEvent<E>)
}
