package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime
import java.util.*

@Serializable
data class TimeProfile(
    val timeSeries: List<TimeSerie>? = null,
    val exceptionTimeSeries: List<ExceptionTimeSerie>? = null,
    val exceptionTimePointSeries: List<ExceptionTimepointSerie>? = null,
    val name: String? = null,
    val description: String? = null,
    val timePointSeries: List<TimePointSerie>? = null,
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
