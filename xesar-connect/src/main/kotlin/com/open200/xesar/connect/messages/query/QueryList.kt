package com.open200.xesar.connect.messages.query

import QueryResource
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.messages.Message
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

/**
 * Represents a query list containing a request ID and a response of type [T].
 *
 * @param requestId The unique identifier of the request.
 * @param response The response of type [T].
 * @param T The type of the response, constrained to implement the [QueryResource] interface.
 */
@Serializable
data class QueryList<out T : QueryResource>(
    @Serializable(with = UUIDSerializer::class) val requestId: UUID,
    val response: Response<T>
) : Message {
    /**
     * Represents the response data of the query list.
     *
     * @param data The list of [T] elements.
     * @param totalCount The total count of elements in the response.
     * @param filterCount The count of elements after applying filters.
     */
    @Serializable
    data class Response<out T>(val data: List<T>, val totalCount: Int, val filterCount: Int)
}

/**
 * Encodes a [QueryList] message to its JSON representation.
 *
 * @param message The [QueryList] message to encode.
 * @return The JSON-encoded string representation of the [QueryList] message.
 * @throws ParsingException if an error occurs during encoding.
 */
inline fun <reified T : QueryResource> encodeQueryList(message: QueryList<T>): String {
    try {
        return jsonFormat.encodeToString(message)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $message", e)
        throw ParsingException()
    }
}
/**
 * Decodes a JSON-encoded [QueryList] message.
 *
 * @param text The JSON-encoded string to decode.
 * @return The decoded [QueryList] message.
 * @throws ParsingException if an error occurs during decoding.
 */
inline fun <reified T : QueryResource> decodeQueryList(text: String): QueryList<T> {
    try {
        return jsonFormat.decodeFromString(text)
    } catch (e: Exception) {
        logger.warn("Couldn't parse $text", e)
        throw ParsingException()
    }
}
