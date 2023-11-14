package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a coding station in the system.
 *
 * @param id The unique identifier of the coding station.
 * @param name The name of the coding station.
 * @param description The description of the coding station (optional).
 * @param partitionId The unique identifier of the partition the coding station belongs to.
 * @param deleted The date and time the coding station was deleted (optional).
 * @param online Indicates if the coding station is online. (optional)
 */
@Serializable
data class CodingStation(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val deleted: LocalDateTime? = null,
    val online: Boolean? = null
) : QueryListResource, QueryElementResource {
    companion object {
        const val QUERY_RESOURCE = "coding-stations"
    }
}
