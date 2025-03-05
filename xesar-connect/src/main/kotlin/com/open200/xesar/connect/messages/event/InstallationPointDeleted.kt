package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete an installation point.
 *
 * @param id The id of the installation point.
 * @param zones The zones of the installation point.
 * @param authorizationProfiles The authorization profiles of the installation point.
 * @param linkedInstallationPoints The linked installation points of the installation point.
 * @param timeProfile The time profile of the installation point.
 */
@Serializable
data class InstallationPointDeleted(
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val zones: List<Int> = emptyList(),
    val authorizationProfiles: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val linkedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
    val timeProfile: @Serializable(with = UUIDSerializer::class) UUID? = null,
) : Event
