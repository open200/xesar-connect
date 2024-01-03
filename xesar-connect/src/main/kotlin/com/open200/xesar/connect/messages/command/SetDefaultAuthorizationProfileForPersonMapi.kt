package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the default authorization profile for a person.
 *
 * @param externalId The external id of the person.
 * @param commandId The id of the command.
 * @param defaultAuthorizationProfileName The name of the default authorization profile. Set null to
 *   remove authorization-profile from person. Empty string is ignored, accept only
 *   authorization-profile names with at least 1 char.
 * @param token The token of the command.
 */
@Serializable
data class SetDefaultAuthorizationProfileForPersonMapi(
    val externalId: String,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val defaultAuthorizationProfileName: String? = null,
    val token: String,
) : Command
