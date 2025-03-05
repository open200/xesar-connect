package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a calendar.
 *
 * @param name The name of the calendar.
 * @param calendarIdentifier The identifier of the calendar.
 * @param specialDays The special days of the calendar.
 * @param id The id of the calendar.
 */
@Serializable
data class CalendarChanged(
    val name: String,
    val calendarIdentifier: Int? = null,
    val specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate>,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : Event
