package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the default validity duration.
 *
 * @param validityDuration The default validity duration.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class SetDefaultValidityDurationMapi(
    val validityDuration: Short,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
