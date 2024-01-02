package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a calendar.
 *
 * @param name The name of the calendar.
 * @param description The description of the coding station.
 * @param id The id of the coding station.
 */
@Serializable
data class CodingStationChanged(
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
