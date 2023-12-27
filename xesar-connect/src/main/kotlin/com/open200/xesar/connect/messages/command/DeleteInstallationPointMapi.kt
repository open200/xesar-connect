package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete an installation point.
 *
 * @param id The id of the installation point to delete.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class DeleteInstallationPointMapi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
