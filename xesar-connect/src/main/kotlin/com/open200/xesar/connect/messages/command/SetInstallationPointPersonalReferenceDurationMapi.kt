package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the installation point personal reference duration.
 *
 * @param commandId The id of the command.
 * @param personalReferenceDuration The personal reference duration.
 * @param token The token of the command.
 */
@Serializable
data class SetInstallationPointPersonalReferenceDurationMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val personalReferenceDuration: PersonalLog,
    val token: String,
) : Command
