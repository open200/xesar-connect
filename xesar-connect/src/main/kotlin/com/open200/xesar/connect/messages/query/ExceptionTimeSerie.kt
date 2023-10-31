package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ExceptionTimeSerie(
    val times: List<TimeSerie.TimeRange>,
    val calendars: List<@Serializable(with = UUIDSerializer::class) UUID>
)
