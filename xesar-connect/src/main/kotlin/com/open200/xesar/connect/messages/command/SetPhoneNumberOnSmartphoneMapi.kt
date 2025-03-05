package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the phone number on a smartphone media.
 *
 * @param commandId The id of the command.
 * @param id The id of the smartphone media.
 * @param phoneNumber The phone number to set.
 * @param token The token of the command.
 */
@Serializable
data class SetPhoneNumberOnSmartphoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val phoneNumber: String,
    val token: String,
) : Command
