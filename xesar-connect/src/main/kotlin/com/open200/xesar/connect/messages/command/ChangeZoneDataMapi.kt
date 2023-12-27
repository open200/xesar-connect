package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change data of a zone.
 *
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param id The id of the zone.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class ChangeZoneDataMapi(
    val name: String,
    val description: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
