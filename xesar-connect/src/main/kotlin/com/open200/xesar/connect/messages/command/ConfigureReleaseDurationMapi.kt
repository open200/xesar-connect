package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to configure the release duration.
 *
 * @param commandId The id of the command
 * @param releaseDurationShort The short release duration value (i.e. how long should the door stay
 *   open) measured in seconds.
 * @param releaseDurationLong The long release duration value (i.e. how long should the door stay
 *   open) measured in seconds.
 * @param id The installation point id.
 * @param token The token of the command.
 */
@Serializable
data class ConfigureReleaseDurationMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val releaseDurationShort: Int? = null,
    val releaseDurationLong: Int? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
