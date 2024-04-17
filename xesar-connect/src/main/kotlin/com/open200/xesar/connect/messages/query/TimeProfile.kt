package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.messages.*
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data class TimeProfile(
    val timeSeries: List<TimeSerie>,
    val exceptionTimeSeries: List<ExceptionTimeSerie>,
    val exceptionTimePointSeries: List<ExceptionTimepointSerie>? = emptyList(),
    val name: String? = null,
    val description: String? = null,
    val timePointSeries: List<TimePointSerie>? = emptyList(),
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val type: TimeProfileType? = null,
    val validStandardTimeProfile: Boolean? = null
) : QueryListResource, QueryElementResource {
    companion object {
        const val QUERY_RESOURCE = "time-profiles"
    }
}
