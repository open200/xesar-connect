package com.open200.xesar.connect.filters

import java.util.*

class ApiErrorFilter(correlationId: UUID, private val aTopic: String) : MessageFilter {

    private val correlationIdRegex =
        Regex(
            "\"${UUIDJsonName.correlationId}\":\\s*\"${Regex.fromLiteral(correlationId.toString())}\"")

    override fun filter(topic: String, message: String): Boolean {
        return message.contains(correlationIdRegex) && aTopic == topic
    }
}
