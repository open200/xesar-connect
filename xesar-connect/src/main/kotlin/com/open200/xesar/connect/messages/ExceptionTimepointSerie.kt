package com.open200.xesar.connect.messages

import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a set of time points that are valid at exceptional days (defined using a calendar).
 *
 * @param points The list of time points.
 * @param calendars The list of calendars associated with the time point serie.
 */
@Serializable
data class ExceptionTimepointSerie(
    val calendars: List<@Serializable(with = UUIDSerializer::class) UUID>? = null,
    val points: List<@Serializable(with = LocalTimeSerializer::class) LocalTime>? = null
)
