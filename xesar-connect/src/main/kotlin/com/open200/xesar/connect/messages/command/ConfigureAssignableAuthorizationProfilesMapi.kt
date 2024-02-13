package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to specify which authorization profiles can be assigned to a user
 * group.
 *
 * @param commandId The id of the command
 * @param assignableAuthorizationProfiles The list of authorization profile ids.
 * @param id The user group id.
 * @param token The token of the command.
 */
@Serializable
data class ConfigureAssignableAuthorizationProfilesMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val assignableAuthorizationProfiles: List<@Serializable(with = UUIDSerializer::class) UUID>,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
