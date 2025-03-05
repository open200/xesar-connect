package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to lock a medium.
 *
 * @param commandId The id of the command.
 * @param id The id of a medium.
 * @param token The token of the command.
 */
@Serializable
data class LockMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
