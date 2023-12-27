package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to remove a zone authorization from a medium.
 *
 * @param authorization The id of the authorization.
 * @param id The id of the medium.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class RemoveZoneAuthorizationFromMediumMapi(
    @Serializable(with = UUIDSerializer::class) val authorization: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
