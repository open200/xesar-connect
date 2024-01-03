package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to add a zone authorization to a medium.
 *
 * @param commandId The id of the command.
 * @param id The id of the medium.
 * @param token The token of the command.
 * @param authorization The authorization data.
 */
@Serializable
data class AddZoneAuthorizationToMediumMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
    val authorization: AuthorizationData? = null
) : Command
