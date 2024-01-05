package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the default value for personal reference duration in the
 * installation settings.
 *
 * @param commandId The id of the command.
 * @param personalReferenceDuration The personal reference duration.
 * @param token The token of the command.
 */
@Serializable
data class SetPersonPersonalReferenceDurationMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val personalReferenceDuration: PersonalLog,
    val token: String
) : Command
