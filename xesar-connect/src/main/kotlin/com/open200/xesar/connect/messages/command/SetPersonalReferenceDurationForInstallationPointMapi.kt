package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the personal reference duration for an installation point.
 *
 * @param commandId The id of the command.
 * @param personalReferenceDuration The personal reference duration.
 * @param id The id of the installation point.
 * @param token The token of the command.
 */
@Serializable
data class SetPersonalReferenceDurationForInstallationPointMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val personalReferenceDuration: PersonalLog,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
