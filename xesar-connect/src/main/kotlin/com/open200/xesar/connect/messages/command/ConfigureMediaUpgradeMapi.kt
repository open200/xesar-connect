package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to activate/deactivate the XVN functionality for an installation point.
 *
 * @param commandId The id of the command
 * @param upgradeMedia true to activate / false to deactivate XVN functionality.
 * @param id The installation point id.
 * @param token The token of the command.
 */
@Serializable
data class ConfigureMediaUpgradeMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val upgradeMedia: Boolean? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
