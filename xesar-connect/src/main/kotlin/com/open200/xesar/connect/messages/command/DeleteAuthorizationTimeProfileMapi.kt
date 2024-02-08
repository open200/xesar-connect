package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete an authorization time profile.
 *
 * @param commandId The id of the command
 * @param id The time profile id.
 * @param token The token of the command.
 */
@Serializable
data class DeleteAuthorizationTimeProfileMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
