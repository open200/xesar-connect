package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.EntityType
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to add new custom data fields to an entity.
 *
 * @param commandId The id of the command.
 * @param entityType The type of the entity.
 * @param names The list of new field names.
 * @param token The token of the command.
 */
@Serializable
data class AddEntityMetadataDefinitionMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val entityType: EntityType,
    val names: List<String>,
    val token: String,
) : Command
