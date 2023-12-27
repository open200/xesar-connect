package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete a calendar.
 *
 * @param calendarIdentifier The identifier of the calendar.
 * @param id The id of the calendar.
 */
@Serializable
data class CalendarDeleted(
    val calendarIdentifier: Int,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
