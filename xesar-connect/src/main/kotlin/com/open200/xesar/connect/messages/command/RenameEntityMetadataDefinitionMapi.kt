package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.EntityType
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to rename a custom data field of an entity.
 *
 * @param commandId The id of the command.
 * @param entityType The type of the entity.
 * @param metadataDefinitionId The id of the custom data field.
 * @param name The new name of the custom data field.
 * @param token The token of the command.
 */
@Serializable
data class RenameEntityMetadataDefinitionMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val entityType: EntityType,
    val metadataDefinitionId: @Serializable(with = UUIDSerializer::class) UUID,
    val name: String,
    val token: String,
) : Command
