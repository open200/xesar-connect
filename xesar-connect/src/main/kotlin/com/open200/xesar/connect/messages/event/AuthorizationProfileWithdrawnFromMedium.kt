package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to withdraw an authorization profile from a
 * medium.
 *
 * @param accessBeginAt The date and time from which the authorization profile is valid.
 * @param individualAuthorizationProfileIds The list of individual authorization profile IDs.
 * @param authorizationProfileId The authorization profile ID.
 * @param withdrawnAt The date and time at which the authorization profile was withdrawn.
 * @param validUntil The date and time until which the authorization profile is valid.
 * @param id The ID of the medium
 * @param validFrom The date and time from which the authorization profile is valid.
 * @param accessEndAt The date and time until which the authorization profile is valid.
 */
@Serializable
data class AuthorizationProfileWithdrawnFromMedium(
    val accessBeginAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val individualAuthorizationProfileIds: List<@Serializable(with = UUIDSerializer::class) UUID>? =
        emptyList(),
    val authorizationProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val withdrawnAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val validUntil: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val validFrom: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val accessEndAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null
) : Event
