package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to remove a zone authorization from a medium.
 *
 * @param commandId The id of the command.
 * @param authorization The id of the authorization.
 * @param id The id of the medium.
 * @param token The token of the command.
 */
@Serializable
data class RemoveZoneAuthorizationFromMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val authorization: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
