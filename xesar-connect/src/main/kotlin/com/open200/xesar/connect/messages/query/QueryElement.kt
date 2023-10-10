package com.open200.xesar.connect.messages.query

import QueryResource
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.messages.Message
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import mu.KotlinLogging

/**
 * Represents a query element containing a request ID and a response of type [T].
 *
 * @param requestId The unique identifier of the request.
 * @param response The response of type [T].
 * @param T The type of the response, constrained to implement the [QueryResource] interface.
 */
@Serializable
data class QueryElement<out T : QueryResource>(
    @Serializable(with = UUIDSerializer::class) val requestId: UUID,
    val response: T
) : Message

val jsonFormat = Json {
    encodeDefaults = true
    ignoreUnknownKeys = true
}

val logger = KotlinLogging.logger {}

/**
 * Encodes a [QueryElement] message to its JSON representation.
 *
 * @param message The [QueryElement] message to encode.
 * @return The JSON-encoded string representation of the [QueryElement] message.
 * @throws ParsingException if an error occurs during encoding.
 */
inline fun <reified T : QueryResource> encodeQueryElement(message: QueryElement<T>): String {
    try {
        return jsonFormat.encodeToString(message)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $message", e)
        throw ParsingException()
    }
}

/**
 * Decodes a JSON-encoded [QueryElement] message.
 *
 * @param text The JSON-encoded string to decode.
 * @return The decoded [QueryElement] message.
 * @throws ParsingException if an error occurs during decoding.
 */
inline fun <reified T : QueryResource> decodeQueryElement(text: String): QueryElement<T> {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $text", e)
        throw ParsingException()
    }
}
