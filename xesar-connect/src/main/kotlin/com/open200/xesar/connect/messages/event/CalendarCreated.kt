package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create a calendar.
 *
 * @param partitionId The partition id of the calendar.
 * @param calendarIdentifier The identifier of the calendar.
 * @param name The name of the calendar.
 * @param specialDays The special days of the calendar.
 * @param id The id of the calendar.
 */
@Serializable
data class CalendarCreated(
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val calendarIdentifier: Int,
    val name: String,
    val specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate>,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
