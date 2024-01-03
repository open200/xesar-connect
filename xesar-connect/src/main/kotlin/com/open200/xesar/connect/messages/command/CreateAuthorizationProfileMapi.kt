package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to configure the office mode time profile.
 *
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile.
 * @param id The authorization profile id.
 * @param commandId The id of the command
 * @param token The token of the command.
 */
@Serializable
data class CreateAuthorizationProfileMapi(
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
