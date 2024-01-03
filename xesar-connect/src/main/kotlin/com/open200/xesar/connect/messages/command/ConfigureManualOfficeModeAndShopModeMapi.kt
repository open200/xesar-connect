package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to configure the manual office mode and shop mode.
 *
 * @param shopMode The shop mode.
 * @param manualOfficeMode The manual office mode.
 * @param id The installation point id.
 * @param commandId The id of the command
 * @param token The token of the command.
 */
@Serializable
data class ConfigureManualOfficeModeAndShopModeMapi(
    val shopMode: Boolean? = null,
    val manualOfficeMode: Boolean? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
