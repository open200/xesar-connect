package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to remotely disengage an online component
 *
 * @param commandId The command id.
 * @param installationPointId The installation point id.
 * @param extended false for SHORT / true for LONG disengage
 * @param token The token used for authentication
 */
@Serializable
data class RemoteDisengage(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val installationPointId: UUID,
    val extended: Boolean? = null,
    val token: String
) : Command
