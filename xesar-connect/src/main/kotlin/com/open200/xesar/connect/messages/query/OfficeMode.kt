package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.ExceptionTimepointSerie
import com.open200.xesar.connect.messages.TimePointSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an office mode in the system.
 *
 * @param id The installation point id like parameter installationPointId.
 * @param timeProfileId The unique identifier of the time profile associated with the office mode.
 * @param timeProfileName The name of the time profile associated with the office mode.
 * @param timeProfileDetails The details of the time profile associated with the office mode.
 * @param installationPointId The unique identifier of the installation point associated with the
 *   office mode.
 * @param installationPointName The name of the installation point associated with the office mode.
 * @param installationPointDescription The description of the installation point associated with the
 *   office mode.
 * @param installationType The type of the installation point associated with the office mode.
 * @param shopMode The shop mode of the office mode.
 * @param manualOfficeMode The manual office mode of the office mode.
 * @param timeSeries The time series of the office mode.
 * @param exceptionTimeSeries The exception time series of the office mode.
 * @param timePointSeries The time point series of the office mode.
 * @param exceptionTimePointSeries The exception time point series of the office mode.
 */
@Serializable
data class OfficeMode(
    val timeProfileDetails: String? = null,
    val timeProfileName: String,
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

    companion object {
        const val QUERY_RESOURCE = "office-modes"
    }
}
