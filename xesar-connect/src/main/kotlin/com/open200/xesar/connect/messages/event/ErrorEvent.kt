package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import io.ktor.http.*
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an error event.
 *
 * @param reason Additional information about the error. May be null.
 * @param correlationId Either command-id or query-id. Depends on request.
 * @param error The error code.
 */
@Serializable
data class ErrorEvent(
    val reason: String? = null,
    @Serializable(with = UUIDSerializer::class) val correlationId: UUID,
    val error: Int
) : Event
