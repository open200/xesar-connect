package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the default disengage period for a person.
 *
 * @param commandId The id of the command.
 * @param disengagePeriod The disengage period of the person. How long should the installation point
 *   be disengaged. The values for SHORT/LONG in seconds can be changed in the installation
 *   settings.
 * @param externalId The external id of the person.
 * @param token The token of the command.
 */
@Serializable
data class SetDefaultDisengagePeriodForPersonMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val disengagePeriod: DisengagePeriod,
    val externalId: String,
    val token: String
) : Command
