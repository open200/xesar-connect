package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change a custom data field value for a person.
 *
 * @param commandId The id of the command.
 * @param externalId The external id of the person.
 * @param id The id of the person.
 * @param metadataId The id of the custom data field.
 * @param value The new value of the custom data field.
 * @param token The token of the command.
 */
@Serializable
data class ChangePersonMetadataValueMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val externalId: String? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val metadataId: @Serializable(with = UUIDSerializer::class) UUID,
    val value: String,
    val token: String,
) : Command
