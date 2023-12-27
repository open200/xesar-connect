package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to request to add a medium to an installation.
 *
 * @param hardwareId The hardware id of the medium.
 * @param id The id of the medium.
 * @param terminalId The id of the terminal.
 * @param label The label of the medium.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class RequestAddMediumToInstallationMapi(
    val hardwareId: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val terminalId: UUID,
    val label: String? = null,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
