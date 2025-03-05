package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete a coding station.
 *
 * @param deletedAt The date and time when the coding station was deleted.
 * @param id The id of the coding station.
 */
@Serializable
data class CodingStationDeleted(
    val deletedAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
) : Event
