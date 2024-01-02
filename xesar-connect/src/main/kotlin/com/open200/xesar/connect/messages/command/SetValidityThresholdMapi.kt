package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the validity threshold.
 *
 * @param commandId The id of the command.
 * @param validityThreshold The validity threshold.
 * @param token The token of the command.
 */
@Serializable
data class SetValidityThresholdMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val validityThreshold: Short,
    val token: String
) : Command
