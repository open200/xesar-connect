package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change an authorization profile.
 *
 * @param installationPoints The installation point authorization ids.
 * @param manualOfficeMode Indicates if manual office mode is enabled.
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile.
 * @param standardTimeProfile The standard time profile id.
 * @param id The id of the authorization profile.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class ChangeAuthorizationProfileMapi(
    val installationPoints: List<Authorization>,
    val manualOfficeMode: Boolean,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val standardTimeProfile: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val zones: List<Authorization>,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
