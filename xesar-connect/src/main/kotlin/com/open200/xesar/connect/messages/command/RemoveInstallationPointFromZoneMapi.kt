package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to remove an installation point from a zone.
 *
 * @param commandId The id of the command.
 * @param installationPointId The installation point id.
 * @param id The id of the zone.
 * @param token The token of the command.
 */
@Serializable
data class RemoveInstallationPointFromZoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val installationPointId: UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
