package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to specify which authorization profiles can be assigned to a user
 * group.
 *
 * @param assignableAuthorizationProfiles The list of authorization profile ids.
 * @param id The user group id.
 * @param commandId The id of the command
 * @param token The token of the command.
 */
@Serializable
data class ConfigureAssignableAuthorizationProfilesMapi(
    val assignableAuthorizationProfiles: List<@Serializable(with = UUIDSerializer::class) UUID>,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
