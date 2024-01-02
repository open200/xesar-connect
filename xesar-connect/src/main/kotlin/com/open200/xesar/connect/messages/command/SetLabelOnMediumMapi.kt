package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the label on a medium.
 *
 * @param commandId The id of the command.
 * @param id The id of the medium.
 * @param label The label to set.
 * @param token The token of the command.
 */
@Serializable
data class SetLabelOnMediumMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val label: String,
    val token: String,
) : Command
