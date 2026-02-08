package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to revoke a smartphone medium.
 *
 * @param commandId The id of the command.
 * @param id The id of the smartphone medium.
 * @param token The token of the command.
 */
@Serializable
data class RevokeSmartphoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val token: String,
) : Command
