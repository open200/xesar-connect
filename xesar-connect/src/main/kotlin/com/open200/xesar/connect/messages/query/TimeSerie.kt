package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.LocalTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime

@Serializable
data class TimeSerie(val times: List<TimeRange>, val days: List<Weekday>) {
    @Serializable
    data class TimeRange(
        @Serializable(with = LocalTimeSerializer::class) val start: LocalTime,
        @Serializable(with = LocalTimeSerializer::class) val end: LocalTime
    )
}
