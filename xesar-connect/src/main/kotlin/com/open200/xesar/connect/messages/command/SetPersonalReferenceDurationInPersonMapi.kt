package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the personal reference duration for a person.
 *
 * @param commandId The id of the command.
 * @param newValue The personal reference duration.
 * @param externalId The external id of the person.
 * @param token The token of the command.
 */
@Serializable
data class SetPersonalReferenceDurationInPersonMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val newValue: PersonalLog,
    val externalId: String,
    val token: String,
) : Command
