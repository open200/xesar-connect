package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the personal reference duration for a person.
 *
 * @param newValue The personal reference duration.
 * @param externalId The external id of the person.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class SetPersonalReferenceDurationInPersonMapi(
    val newValue: PersonalLog,
    val externalId: String,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
