package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the access end of an identification medium
 *
 * @param accessEndAt The access end (Date and Time).
 * @param id The id of the medium.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class SetAccessEndAtMapi(
    @Serializable(with = LocalDateTimeSerializer::class) val accessEndAt: LocalDateTime? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String,
) : Command
