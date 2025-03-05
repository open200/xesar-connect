package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to prepare the removal of an EVVA component.
 *
 * @param aggregateId The id of the EVVA component to remove.
 * @param stateChangeAt The time of the event.
 * @param linkedInstallationPoints The linked installation points.
 */
@Serializable
data class EvvaComponentRemovalPrepared(
    val aggregateId: @Serializable(with = UUIDSerializer::class) UUID,
    val stateChangeAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val linkedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
) : Event
