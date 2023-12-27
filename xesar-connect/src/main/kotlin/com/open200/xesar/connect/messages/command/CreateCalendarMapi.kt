package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to create a calendar.
 *
 * @param name The name of the calendar.
 * @param specialDays A list of special days.
 * @param id The id of the calendar.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class CreateCalendarMapi(
    val name: String,
    val specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
