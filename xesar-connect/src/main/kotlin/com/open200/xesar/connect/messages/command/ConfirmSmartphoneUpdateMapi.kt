package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to confirm a smartphone update in Self Service Mode.
 *
 * @param commandId The id of the command.
 * @param mediumId The id of the smartphone medium.
 * @param transactionId The id of the transaction.
 * @param token The token of the command.
 */
@Serializable
data class ConfirmSmartphoneUpdateMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val mediumId: @Serializable(with = UUIDSerializer::class) UUID,
    val transactionId: @Serializable(with = UUIDSerializer::class) UUID,
    val token: String,
) : Command
