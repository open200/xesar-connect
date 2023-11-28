package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a set of time intervals that are valid at exceptional days (defined using a calendar).
 *
 * @param times The list of time ranges.
 * @param calendars The list of calendars associated with the time serie.
 */
@Serializable
data class ExceptionTimeSerie(
    val times: List<TimeSerie.TimeRange>,
    val calendars: List<@Serializable(with = UUIDSerializer::class) UUID>
)
