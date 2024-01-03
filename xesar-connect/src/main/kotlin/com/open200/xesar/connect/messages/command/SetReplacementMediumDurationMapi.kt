package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the replacement medium duration.
 *
 * @param replacementMediumDuration The replacement medium duration.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class SetReplacementMediumDurationMapi(
    val replacementMediumDuration: Short,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
