package com.open200.xesar.connect

import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.Event

fun interface EventHandler<E : Event> {
    fun handle(apiEvent: ApiEvent<E>)
}
