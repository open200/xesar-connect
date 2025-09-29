package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.MobileServiceMode
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the mobile service mode.
 *
 * @param commandId The id of the command.
 * @param mobileServiceMode Enum for the Mobile Service mode. Used to describe the application's
 *   method of communication with Smartphones. Allowed values: XMS, SELF_SERVICE.
 * @param token The token of the command.
 */
@Serializable
data class SetMobileServiceModeMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val mobileServiceMode: MobileServiceMode,
    val token: String,
) : Command
