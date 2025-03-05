package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change installation points in a zone.
 *
 * @param accessId The access ID of the zone.
 * @param addedInstallationPoints The list of installation points that were added to the zone.
 * @param aggregateId The id of the zone.
 * @param removedInstallationPoints The list of installation points that were removed from the zone.
 */
@Serializable
data class InstallationPointsInZoneChanged(
    val accessId: Int,
    val addedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
    @Serializable(with = UUIDSerializer::class) val aggregateId: UUID,
    val removedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
) : Event
