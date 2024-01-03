package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create a coding station.
 *
 * @param partitionId The partition id of the coding station.
 * @param name The name of the coding station.
 * @param description The description of the coding station.
 * @param id The id of the coding station.
 */
@Serializable
data class CodingStationCreated(
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
