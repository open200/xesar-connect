package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to assign a person to a medium.
 *
 * @param commandId The id of the command.
 * @param id The id of the medium.
 * @param personId The id of the person.
 * @param token The token of the command.
 */
@Serializable
data class AssignPersonToMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val personId: UUID,
    val token: String
) : Command
