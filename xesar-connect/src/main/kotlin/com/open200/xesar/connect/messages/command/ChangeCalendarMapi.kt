package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change a calendar.
 *
 * @param commandId The id of the command.
 * @param name The name of the calendar.
 * @param specialDays The special days of the calendar.
 * @param id The id of the calendar.
 * @param token The token of the command.
 */
@Serializable
data class ChangeCalendarMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val name: String,
    val specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
