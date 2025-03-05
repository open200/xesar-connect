package com.open200.xesar.connect.messages

import com.open200.xesar.connect.utils.LocalTimeSerializer
import java.time.LocalTime
import kotlinx.serialization.Serializable

/**
 * Represents a set of time points that are valid on specific days.
 *
 * @param days The list of days associated with the time points.
 * @param points The list of time points.
 */
@Serializable
data class TimePointSerie(
    val days: List<Weekday>? = null,
    val points: List<@Serializable(with = LocalTimeSerializer::class) LocalTime>? = null,
)
