package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to create a coding station.
 *
 * @param commandId The id of the command.
 * @param name The name of the coding station.
 * @param description The description of the coding station.
 * @param id The id of the coding station.
 * @param token The token of the command.
 */
@Serializable
data class CreateCodingStationMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
