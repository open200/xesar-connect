package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change an installation point.
 *
 * @param commandId The id of the command.
 * @param aggregateId The id of the installation point.
 * @param installationType The type of the installation.
 * @param name The name of the installation point.
 * @param description The description of the installation point.
 * @param installationId The id of the installation.
 * @param token The token of the command.
 */
@Serializable
data class ChangeInstallationPointMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val aggregateId: UUID,
    val installationType: String? = null,
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val installationId: UUID? = null,
    val token: String
) : Command
