package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change of an entity metadata definition.
 *
 * @param entityMetadata Contains the information of the metadata definitions.
 * @param id The id of the updated entity.
 */
@Serializable
data class MetadataDefinitionsUpdated(
    val entityMetadata: List<EntityMetadata>,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : Event
