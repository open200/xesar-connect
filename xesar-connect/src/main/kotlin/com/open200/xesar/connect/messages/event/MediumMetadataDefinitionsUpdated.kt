package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change of a medium metadata definition.
 *
 * @param entityMetadata Contains the information of the metadata definitions.
 * @param id The id of the updated medium.
 */
@Serializable
data class MediumMetadataDefinitionsUpdated(
    val entityMetadata: List<EntityMetadata>,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : Event
