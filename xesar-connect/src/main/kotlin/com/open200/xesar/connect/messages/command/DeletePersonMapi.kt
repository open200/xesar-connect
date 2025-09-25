package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete a person.
 *
 * @param commandId The id of the command.
 * @param externalId The external id of the person to delete.
 * @param id The id of the person.
 * @param token The token of the command.
 */
@Serializable
data class DeletePersonMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val externalId: String? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val token: String,
) : Command
