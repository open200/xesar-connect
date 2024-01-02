package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the validity duration.
 *
 * @param commandId The id of the command.
 * @param validityDuration The validity duration.
 * @param id The id of a medium.
 * @param token The token of the command.
 */
@Serializable
data class SetValidityDurationMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val validityDuration: Short? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
