package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete a zone.
 *
 * @param installationPointIds The corresponding installation points of the zone.
 * @param deletedAt The date and time when the zone was deleted.
 * @param id The id of the zone.
 * @param authorizationProfileIds The corresponding authorization profiles of the zone.
 */
@Serializable
data class ZoneDeleted(
    val installationPointIds: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val deletedAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val authorizationProfileIds: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList()
) : Event
