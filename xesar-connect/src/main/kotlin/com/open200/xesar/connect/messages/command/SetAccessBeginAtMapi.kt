package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the access begin of an identification medium
 *
 * @param commandId The id of the command.
 * @param accessBeginAt The access begin (Date and Time).
 * @param id The id of the medium.
 * @param token The token of the command.
 */
@Serializable
data class SetAccessBeginAtMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val accessBeginAt: LocalDateTime? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String,
) : Command
