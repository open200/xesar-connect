package com.open200.xesar.connect.filters

import java.util.*

/**
 * Implementation of the MessageFilter interface that filters messages based on a specific query ID.
 *
 * @param requestId The query ID to filter by.
 */
class QueryIdFilter(requestId: UUID) : MessageFilter {
    private val requestIdRegex =
        Regex("\"requestId\":\\s*\"${Regex.fromLiteral(requestId.toString())}\"")

    /**
     * Filters the given topic and message based on the query ID.
     *
     * @param topic The topic of the message.
     * @param message The message payload.
     * @return `true` if the message matches the query ID, `false` otherwise.
     */
    override fun filter(topic: String, message: String): Boolean {
        return message.contains(requestIdRegex)
    }
}
