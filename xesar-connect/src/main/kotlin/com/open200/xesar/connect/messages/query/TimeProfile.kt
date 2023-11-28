package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalTime
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data class TimeProfile(
    val timeSeries: List<TimeSerie>,
    val exceptionTimeSeries: List<ExceptionTimeSerie>,
    val exceptionTimePointSeries: List<ExceptionTimepointSerie>? = emptyList(),
    val name: String,
    val description: String? = null,
    val timePointSeries: List<TimePointSerie>? = emptyList(),
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val type: TimeProfileType? = null,
    val validStandardTimeProfile: Boolean? = null
) : QueryListResource, QueryElementResource {
    companion object {
        const val QUERY_RESOURCE = "time-profiles"
    }

    @Serializable
    data class ExceptionTimepointSerie(
        val calendars: List<@Serializable(with = UUIDSerializer::class) UUID>? = null,
        val points: List<@Serializable(with = LocalTimeSerializer::class) LocalTime>? = null
    )

    @Serializable
    data class TimePointSerie(
        val days: List<Weekday>? = null,
        val points: List<@Serializable(with = LocalTimeSerializer::class) LocalTime>? = null
    )
}
