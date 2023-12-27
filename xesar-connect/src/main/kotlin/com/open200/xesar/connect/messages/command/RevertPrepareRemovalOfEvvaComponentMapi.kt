package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to revert a prepare removal of an EVVA component.
 *
 * @param id The id of the EVVA component.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class RevertPrepareRemovalOfEvvaComponentMapi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String,
) : Command
