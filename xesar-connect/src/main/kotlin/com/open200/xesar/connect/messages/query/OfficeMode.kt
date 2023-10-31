package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalTime
import java.util.*

@Serializable
data class OfficeMode(
    val timeProfileDetails: String? = null,
    val timeProfileName: String? = null,
    val exceptionTimePointSeries: List<ExceptionTimepointSerie>? = null,
    @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID,
    val installationPointDescription: String? = null,
    val installationPointName: String? = null,
    val shopMode: Boolean? = null,
    val timeSeries: List<TimeSerie>? = null,
    val installationType: String? = null,
    val manualOfficeMode: Boolean? = null,
    val exceptionTimeSeries: List<ExceptionTimeSerie>? = null,
    @Serializable(with = UUIDSerializer::class) val installationPointId: UUID,
    val timePointSeries: List<TimePointSerie>? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : QueryListResource, QueryElementResource {
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

    companion object {
        const val QUERY_RESOURCE = "office-modes"
    }
}
