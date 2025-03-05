package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to create a zone.
 *
 * @param commandId The id of the command
 * @param installationPoints The list of installation point ids.
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param id The zone id.
 * @param token The token of the command.
 */
@Serializable
data class CreateZoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
