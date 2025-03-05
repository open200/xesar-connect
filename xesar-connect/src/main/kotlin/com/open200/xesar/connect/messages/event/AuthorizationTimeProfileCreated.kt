package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.messages.query.TimeProfileType
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create a time profile.
 *
 * @param timeSeries The time series of the time profile.
 * @param exceptionTimeSeries The exception time series of the time profile.
 * @param name The name of the time profile.
 * @param description The description of the time profile.
 * @param id The id of the time profile.
 * @param type The type of the time profile.
 * @param validStandardTimeProfile Indicates if the time profile is a valid standard time profile.
 */
@Serializable
data class AuthorizationTimeProfileCreated(
    val timeSeries: List<TimeSerie> = emptyList(),
    val exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val type: TimeProfileType,
    val validStandardTimeProfile: Boolean,
) : Event
