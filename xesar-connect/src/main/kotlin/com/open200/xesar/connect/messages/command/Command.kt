package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.Message
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/** Represents a command message in the system. */
@Serializable sealed interface Command : Message

/**
 * Encodes a message of type [T] to its JSON representation.
 *
 * @param message The message to encode.
 * @return The JSON representation of the message.
 */
inline fun <reified T> encodeCommand(message: T): String {
    val jsonFormat = Json { encodeDefaults = true }
    return jsonFormat.encodeToString(message)
}
