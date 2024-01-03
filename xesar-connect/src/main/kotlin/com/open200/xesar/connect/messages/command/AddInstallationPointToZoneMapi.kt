package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to add an installation point to a zone.
 *
 * @param commandId The id of the command.
 * @param installationPointId The id of the installation point.
 * @param id The id of the zone.
 * @param token The token of the command.
 */
@Serializable
data class AddInstallationPointToZoneMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    @Serializable(with = UUIDSerializer::class) val installationPointId: UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
