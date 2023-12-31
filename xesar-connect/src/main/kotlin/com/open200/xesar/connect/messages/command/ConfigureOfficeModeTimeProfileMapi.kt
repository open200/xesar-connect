package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to configure the office mode time profile.
 *
 * @param id The installation point id.
 * @param commandId The id of the command
 * @param timeProfileId The id of the time profile. Null value for removing assigned office mode
 *   time profile.
 * @param token The token of the command.
 */
@Serializable
data class ConfigureOfficeModeTimeProfileMapi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID? = null,
    val token: String
) : Command
