package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to revert a prepare removal of an EVVA
 * component.
 *
 * @param id The id of the EVVA component.
 * @param stateChangedAt The date and time when the state of the EVVA component was changed.
 * @param linkedInstallationPoints The list of linked installation points.
 */
@Serializable
data class PrepareEvvaComponentRemovalReverted(
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val stateChangedAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val linkedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList()
) : Event
