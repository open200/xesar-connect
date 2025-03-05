package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the default validity duration for a smartphone.
 *
 * @param commandId The id of the command.
 * @param validityDuration The validity duration (Min: 1, Max: 1095 days).
 * @param token The token of the command.
 */
@Serializable
data class SetDefaultSmartphoneValidityDurationMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val validityDuration: Short,
    val token: String,
) : Command
