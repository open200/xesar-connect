package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to assign an authorization profile to a medium.
 *
 * @param authorizationProfileId The id of the authorization profile.
 * @param id The id of the medium.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class AssignAuthorizationProfileToMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val authorizationProfileId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
