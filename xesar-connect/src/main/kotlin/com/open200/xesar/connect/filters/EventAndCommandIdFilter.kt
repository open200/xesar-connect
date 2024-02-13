package com.open200.xesar.connect.filters

import java.util.UUID

class EventAndCommandIdFilter(commandId: UUID, private val aTopic: String) : MessageFilter {

    private val commandIdRegex =
        Regex("\"commandId\":\\s*\"${Regex.fromLiteral(commandId.toString())}\"")

    override fun filter(topic: String, message: String): Boolean {
        return message.contains(commandIdRegex) && aTopic == topic
    }
}
