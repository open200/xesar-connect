package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete a person.
 *
 * @param commandId The id of the command.
 * @param externalId The external id of the person to delete.
 * @param token The token of the command.
 */
@Serializable
data class DeletePersonMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val externalId: String? = null,
    val token: String
) : Command
