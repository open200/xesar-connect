package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a zone.
 *
 * @param installationPoints The list of installation points of the zone (optional).
 * @param partitionId The partition identifier of the zone.
 * @param installationPointCount The installationPointCount of the zone (optional).
 * @param name The name of the zone.
 * @param description The description of the zone (optional).
 * @param id The id of the zone.
 * @param entityMetadata Contains the information for all defined custom data fields for the zone
 *   (optional).
 */
@Serializable
data class Zone(
    val installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val installationPointCount: Int? = null,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val entityMetadata: List<EntityMetadata>? = null,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "zones"
    }
}
