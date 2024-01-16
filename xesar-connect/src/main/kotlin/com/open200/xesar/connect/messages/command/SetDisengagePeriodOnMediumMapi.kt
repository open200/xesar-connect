package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the disengage period on a medium.
 *
 * @param commandId The id of the command.
 * @param disengagePeriod The disengage period of the medium. How long should the installation point
 *   be disengaged. The values for SHORT/LONG in seconds can be changed in the installation
 *   settings.
 * @param id The id of the medium.
 * @param token The token of the command.
 */
@Serializable
data class SetDisengagePeriodOnMediumMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val disengagePeriod: DisengagePeriod,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
