package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to create an authorization time profile.
 *
 * @param commandId The id of the command
 * @param timeSeries The list of time series.
 * @param exceptionTimeSeries The list of exception time series.
 * @param name The name of the authorization time profile.
 * @param description The description of the authorization time profile.
 * @param id The time profile id.
 * @param token The token of the command.
 */
@Serializable
data class CreateAuthorizationTimeProfileMapi(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val timeSeries: List<TimeSerie> = emptyList(),
    val exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
