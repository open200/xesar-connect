package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to unassign a person from a medium.
 *
 * @param commandId The id of the command.
 * @param mediumId The id of the medium.
 * @param token The token of the command.
 */
@Serializable
data class UnassignPersonFromMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val mediumId: @Serializable(with = UUIDSerializer::class) UUID,
    val token: String,
) : Command
