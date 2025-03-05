package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to configure the manual office mode and shop mode.
 *
 * @param commandId The id of the command
 * @param shopMode The shop mode.
 * @param manualOfficeMode The manual office mode.
 * @param id The installation point id.
 * @param token The token of the command.
 */
@Serializable
data class ConfigureManualOfficeModeAndShopModeMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val shopMode: Boolean? = null,
    val manualOfficeMode: Boolean? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    val token: String,
) : Command
