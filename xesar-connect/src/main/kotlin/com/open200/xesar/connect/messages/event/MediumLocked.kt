package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to lock a medium.
 *
 * @param aggregateId The id of the medium.
 * @param installationPoints The installation points of the medium.
 * @param mediumIdentifier The identifier of the medium.
 * @param authorizationProfileId The authorization profile id of the medium.
 * @param individualAuthorizations The individual authorizations of the medium.
 * @param validityEnd The validity end of the medium.
 * @param zones The zones of the medium.
 * @param hasMasterKeyAccess Whether the medium has master key access.
 */
@Serializable
data class MediumLocked(
    val aggregateId: @Serializable(with = UUIDSerializer::class) UUID,
    val installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val mediumIdentifier: Long,
    val authorizationProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val individualAuthorizations: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
    val validityEnd: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val zones: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val hasMasterKeyAccess: Boolean
) : Event
