package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to cange an authorization time profile.
 *
 * @param timeSeries The time series of the authorization time profile.
 * @param exceptionTimeSeries The exception time series of the authorization time profile.
 * @param name The name of the authorization time profile.
 * @param description The description of the authorization time profile.
 * @param id The id of the authorization time profile.
 * @param validStandardTimeProfile Indicates if the authorization time profile is a valid standard
 *   time profile.
 */
@Serializable
data class AuthorizationTimeProfileChanged(
    val timeSeries: List<TimeSerie>,
    val exceptionTimeSeries: List<ExceptionTimeSerie>,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val validStandardTimeProfile: Boolean,
) : Event
