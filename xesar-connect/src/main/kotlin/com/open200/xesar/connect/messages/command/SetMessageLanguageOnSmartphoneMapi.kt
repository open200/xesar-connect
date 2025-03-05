package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the message language on a smartphone media.
 *
 * @param commandId The id of the command.
 * @param messageLanguage The message language to set.
 * @param id The id of the smartphone media.
 * @param token The token of the command.
 */
@Serializable
data class SetMessageLanguageOnSmartphoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val messageLanguage: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
