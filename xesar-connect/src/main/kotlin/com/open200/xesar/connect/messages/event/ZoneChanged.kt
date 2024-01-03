package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a zone.
 *
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param id The id of the zone.
 */
@Serializable
data class ZoneChanged(
    val name: String,
    val description: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
