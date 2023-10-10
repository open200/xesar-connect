package com.open200.xesar.connect.messages.session

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
 * Represents a session containing a session event.
 *
 * @param commandId The unique identifier of the session command.
 * @param event The session event associated with the session.
 */
@Serializable
data class Session<out T : SessionEvent>(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val event: T
) : Message

/**
 * Marker interface for a session event. Implementing this interface indicates that a class is a
 * session event.
 */
interface SessionEvent

val jsonFormat = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}
val logger = KotlinLogging.logger {}

/**
 * Encodes a session into its JSON representation.
 *
 * @param message The session to encode.
 * @return The JSON string representing the session.
 * @throws ParsingException if the session cannot be parsed.
 */
inline fun <reified T : SessionEvent> encodeSession(message: Session<T>): String {
    try {
        return jsonFormat.encodeToString(message)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $message", e)
        throw ParsingException()
    }
}
/**
 * Decodes a JSON string into a session.
 *
 * @param text The JSON string to decode.
 * @return The decoded session.
 * @throws ParsingException if the JSON string cannot be parsed.
 */
inline fun <reified T : SessionEvent> decodeSession(text: String): Session<T> {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $text", e)
        throw ParsingException()
    }
}

/**
 * Decodes a JSON string into an unauthorized login attempt event.
 *
 * @param text The JSON string to decode.
 * @return The decoded unauthorized login attempt event.
 * @throws ParsingException if the JSON string cannot be parsed.
 */
fun decodeUnauthorizedLoginAttempt(text: String): UnauthorizedLoginAttempt {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $text", e)
        throw ParsingException()
    }
}

/**
 * Decodes a JSON string into a logged-out event.
 *
 * @param text The JSON string to decode.
 * @return The decoded logged-out event.
 * @throws ParsingException if the JSON string cannot be parsed.
 */
fun decodeLogout(text: String): LoggedOut {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $text", e)
        throw ParsingException()
    }
}
