package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.ExceptionTimepointSerie
import com.open200.xesar.connect.messages.TimePointSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create an office mode time profile.
 *
 * @param timeSeries The time series of the office mode time profile.
 * @param exceptionTimeSeries The exception time series of the office mode time profile.
 * @param exceptionTimePointSeries The exception time point series of the office mode time profile.
 * @param name The name of the office mode time profile.
 * @param description The description of the office mode time profile.
 * @param timePointSeries The time point series of the office mode time profile.
 * @param id The id of the office mode time profile.
 */
@Serializable
data class OfficeModeTimeProfileCreated(
    val timeSeries: List<TimeSerie>,
    val exceptionTimeSeries: List<ExceptionTimeSerie>,
    val exceptionTimePointSeries: List<ExceptionTimepointSerie>,
    val name: String,
    val description: String? = null,
    val timePointSeries: List<TimePointSerie>,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
