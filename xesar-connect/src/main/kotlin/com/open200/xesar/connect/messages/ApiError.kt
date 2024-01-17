package com.open200.xesar.connect.messages

import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.messages.event.jsonFormat
import com.open200.xesar.connect.messages.event.logger
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

/**
 * Represents an error event.
 *
 * @param reason Additional information about the error. May be null.
 * @param correlationId Either command-id or query-id. Depends on request.
 * @param error The error code.
 */
@Serializable
data class ApiError(
    val reason: String? = null,
    @Serializable(with = UUIDSerializer::class) val correlationId: UUID,
    val error: Int
) : Message

/**
 * Encodes an ApiError into its JSON representation.
 *
 * @param message The ApiError to encode.
 * @return The JSON string representing the ApiError.
 * @throws ParsingException if the ApiError cannot be parsed.
 */
fun encodeError(message: ApiError): String {
    try {
        return jsonFormat.encodeToString(message)
    } catch (e: Exception) {
        logger.warn("Couldn't encode $message", e)
        throw ParsingException()
    }
}

/**
 * Decodes a JSON string into an ApiError.
 *
 * @param text The JSON string to decode.
 * @return The decoded ApiError.
 * @throws ParsingException if the JSON string cannot be parsed.
 */
fun decodeError(text: String): ApiError {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't decode $text", e)
        throw ParsingException()
    }
}
