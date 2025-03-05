package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.TimePointSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change an office mode time profile.
 *
 * @param commandId The id of the command.
 * @param id The id of the time profile.
 * @param name The name of the office mode time profile.
 * @param description The description of the office mode time profile.
 * @param timeSeries The time intervals for allowing/disallowing entry.
 * @param exceptionTimeSeries The time intervals for allowing/disallowing entry at exceptional dates
 *   (holidays etc.).
 * @param exceptionTimePointSeries Time of a day in a specific weekday, used to end the allowed
 *   entry at exceptional dates (holidays etc.).
 * @param timePointSeries Time of a day in a specific weekday, used to end the allowed entry.
 * @param token The token of the command.
 */
@Serializable
data class ChangeOfficeModeTimeProfileMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val description: String? = null,
    val timeSeries: List<TimeSerie>? = emptyList(),
    val exceptionTimeSeries: List<ExceptionTimeSerie>? = emptyList(),
    val exceptionTimePointSeries: List<ExceptionTimeSerie>? = emptyList(),
    val timePointSeries: List<TimePointSerie>? = emptyList(),
    val token: String,
) : Command
