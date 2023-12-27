package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete a person.
 *
 * @param externalId The external id of the person to delete.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class DeletePersonMapi(
    val externalId: String? = null,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
