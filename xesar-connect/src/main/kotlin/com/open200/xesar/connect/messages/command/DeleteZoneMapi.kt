package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete a zone.
 *
 * @param commandId The id of the command.
 * @param id The id of the zone to delete.
 * @param token The token of the command.
 */
@Serializable
data class DeleteZoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
