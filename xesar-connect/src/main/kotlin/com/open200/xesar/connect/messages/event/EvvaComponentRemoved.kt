package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to remove an EVVA component.
 *
 * @param accessId The access ID of the component.
 * @param aggregateId The id of the installation point.
 * @param lockedMediaWithAccessId Locked media with access ID.
 * @param linkedInstallationPointIds The linked installation point ids.
 * @param useOddKey If the odd key is used.
 * @param forced If the component was forcefully removed.
 * @param maintenanceComponentId The maintenance component id.
 * @param stateChangedAt When the state of the evva component was changed.
 * @param nonce The number used once.
 */
@Serializable
data class EvvaComponentRemoved(
    val accessId: Long,
    val aggregateId: @Serializable(with = UUIDSerializer::class) UUID,
    val lockedMediaWithAccessId: List<Long> = emptyList(),
    val linkedInstallationPointIds: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
    val useOddKey: Boolean,
    val forced: Boolean,
    val maintenanceComponentId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val stateChangedAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val nonce: Long,
) : Event
