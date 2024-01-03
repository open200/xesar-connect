package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to add an EVVA component to the system.
 *
 * @param id installation point id.
 * @param type The type of the component.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class AddEvvaComponentMapi(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val type: ComponentType,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
