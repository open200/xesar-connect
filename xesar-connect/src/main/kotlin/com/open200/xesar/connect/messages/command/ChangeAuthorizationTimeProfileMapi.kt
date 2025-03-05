package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to change an authorization time profile.
 *
 * @param commandId The id of the command.
 * @param id The id of the time profile.
 * @param name The name of the time profile.
 * @param description The description of the time profile.
 * @param timeSeries The time series of the time profile.
 * @param exceptionTimeSeries The exception time series of the time profile.
 * @param token The token of the command.
 */
@Serializable
data class ChangeAuthorizationTimeProfileMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val description: String? = null,
    val timeSeries: List<TimeSerie> = emptyList(),
    val exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    val token: String,
) : Command
