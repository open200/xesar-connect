package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to create a person.
 *
 * @param commandId The id of the command.
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.*
 * @param identifier The identifier of the person.
 * @param externalId The external id of the person.
 * @param id The id of the person.
 * @param token The token of the command.
 */
@Serializable
data class CreatePersonMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val firstName: String,
    val lastName: String,
    val identifier: String? = null,
    val externalId: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
