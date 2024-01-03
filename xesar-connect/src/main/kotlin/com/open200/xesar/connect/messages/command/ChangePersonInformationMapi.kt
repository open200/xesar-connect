package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change a person information.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param externalId The external id of the person.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class ChangePersonInformationMapi(
    val firstName: String? = null,
    val lastName: String? = null,
    val identifier: String? = null,
    val externalId: String? = null,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
