package com.open200.xesar.connect.filters

import java.util.UUID

/**
 * Implementation of the MessageFilter interface that filters messages based on a specific command
 * ID.
 *
 * @param commandId The command ID to filter by.
 */
class CommandIdFilter(commandId: UUID) : MessageFilter {

    private val commandIdRegex =
        Regex("\"commandId\":\\s*\"${Regex.fromLiteral(commandId.toString())}\"")

    /**
     * Filters the given topic and message based on the command ID.
     *
     * @param topic The topic of the message.
     * @param message The message payload.
     * @return `true` if the message matches the command ID, `false` otherwise.
     */
    override fun filter(topic: String, message: String): Boolean {
        return message.contains(commandIdRegex)
    }
}
