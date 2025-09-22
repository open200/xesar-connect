package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.EntityType
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to delete custom data fields for an entity.
 *
 * @param commandId The id of the command.
 * @param entityType The type of the entity.
 * @param names The list of field names to delete.
 * @param token The token of the command.
 */
@Serializable
data class DeleteEntityMetadataDefinitionMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val entityType: EntityType,
    val names: List<String>,
    val token: String,
) : Command
