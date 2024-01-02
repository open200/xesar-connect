package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to add an installation point authorization to a medium.
 *
 * @param commandId The id of the command.
 * @param id The id of the medium.
 * @param authorization The authorization data.
 * @param token The token of the command.
 */
@Serializable
data class AddInstallationPointAuthorizationToMediumMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val authorization: AuthorizationData,
    val token: String
) : Command
