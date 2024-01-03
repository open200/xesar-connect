package com.open200.xesar.connect.messages

import com.open200.xesar.connect.utils.LocalTimeSerializer
import java.time.LocalTime
import kotlinx.serialization.Serializable

/**
 * Represents a set of time intervals that are valid on specific days.
 *
 * @param times The list of time ranges.
 * @param days The list of days associated with the time ranges.
 */
@Serializable
data class TimeSerie(val times: List<TimeRange>, val days: List<Weekday>) {
    @Serializable
    data class TimeRange(
        @Serializable(with = LocalTimeSerializer::class) val start: LocalTime,
        @Serializable(with = LocalTimeSerializer::class) val end: LocalTime
    )
}
