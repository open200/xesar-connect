package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.messages.Message
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging

/**
 * Represents a wrapper class containing the resulting event of a command with the corresponding
 * command id.
 *
 * @param commandId Used to correlate commands and their resulting events.
 * @param event the event which results from a command.
 */
@Serializable
data class ApiEvent<out T : Event>(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID? = null,
    val event: T
) : Message

val jsonFormat = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}
val logger = KotlinLogging.logger {}

/**
 * Marker interface for an event. Implementing this interface indicates that a class is an event.
 */
interface Event : Message

/**
 * Encodes an ApiEvent into its JSON representation.
 *
 * @param message The ApiEvent to encode.
 * @return The JSON string representing the ApiEvent.
 * @throws ParsingException if the ApiEvent cannot be parsed.
 */
inline fun <reified T : Event> encodeEvent(message: ApiEvent<T>): String {
    try {
        return jsonFormat.encodeToString(message)
    } catch (e: Exception) {
        logger.warn("Couldn't encode $message", e)
        throw ParsingException()
    }
}

/**
 * Decodes a JSON string into an ApiEvent.
 *
 * @param text The JSON string to decode.
 * @return The decoded ApiEvent.
 * @throws ParsingException if the JSON string cannot be parsed.
 */
inline fun <reified T : Event> decodeEvent(text: String): ApiEvent<T> {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't decode $text", e)
        throw ParsingException()
    }
}
