package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete an authorization profile.
 *
 * @param commandId The id of the command.
 * @param id The id of the authorization profile to delete.
 * @param token The token of the command.
 */
@Serializable
data class DeleteAuthorizationProfileMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command