package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a calendar in the system.
 *
 * @param serialVersionUID The serial version UID.
 * @param partitionId The unique identifier of the partition the calendar belongs to.
 * @param name The name of the calendar.
 * @param specialDays The list of special days associated with the calendar.
 * @param id The unique identifier of the calendar.
 */
@Serializable
data class Calendar(
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val name: String,
    val specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate>,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "calendars"
    }
}
