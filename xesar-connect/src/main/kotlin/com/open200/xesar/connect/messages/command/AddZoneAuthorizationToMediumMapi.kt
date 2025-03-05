package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to add a zone authorization to a medium.
 *
 * @param commandId The id of the command.
 * @param id The id of the medium.
 * @param authorization The authorization data.
 * @param token The token of the command.
 */
@Serializable
data class AddZoneAuthorizationToMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val authorization: AuthorizationData,
    val token: String,
) : Command
