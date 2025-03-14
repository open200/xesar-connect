package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to remotely disengage an installation point permanently.
 *
 * @param commandId The command id.
 * @param installationPointId The installation point id.
 * @param enable true for enable / false for disable.
 * @param token The token used for authentication.
 */
@Serializable
data class RemoteDisengagePermanent(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val installationPointId: UUID,
    val enable: Boolean? = null,
    val token: String,
) : Command
