package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create a zone.
 *
 * @param accessId The access id of the zone.
 * @param partitionId The partition id of the zone.
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param id The id of the zone.
 */
@Serializable
data class ZoneCreated(
    val accessId: Int,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
