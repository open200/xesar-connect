package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.query.BatteryCondition
import com.open200.xesar.connect.messages.query.ComponentStatus
import com.open200.xesar.connect.messages.query.OnlineStatus
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to add an EVVA component.
 *
 * @param id The id of the event.
 * @param evvaComponentId The id of the EVVA component.
 * @param type The type of the EVVA component.
 * @param useOddKey Whether the EVVA component uses an odd key.
 * @param nonce The nonce of the EVVA component.
 * @param stateChangedAt The date and time when the state of the EVVA component was changed.
 * @param batteryWarning Whether the battery of the EVVA component is low.
 * @param batteryCondition The condition of the battery of the EVVA component.
 * @param status The status of the EVVA component.
 * @param onlineStatus The online status of the EVVA component.
 * @param firmwareVersion The firmware version of the EVVA component.
 * @param componentParts The parts of the EVVA component.
 * @param serialNumber The serial number of the EVVA component.
 */
@Serializable
data class EvvaComponentAdded(
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val evvaComponentId: @Serializable(with = UUIDSerializer::class) UUID,
    val type: ComponentType? = null,
    val useOddKey: Boolean? = null,
    val nonce: Long? = null,
    val stateChangedAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val batteryWarning: Boolean? = null,
    val batteryCondition: BatteryCondition? = null,
    val status: ComponentStatus,
    val onlineStatus: OnlineStatus? = null,
    val firmwareVersion: FirmwareVersion? = null,
    val componentParts: List<ComponentPart>? = emptyList(),
    val serialNumber: String? = null
) : Event {

    @Serializable
    data class FirmwareVersion(
        val firmwareVariant: String? = null,
        val major: Int? = null,
        val minor: Int? = null,
        val majorBootloader: Int? = null,
        val mechanicalVersion: String? = null,
        val internalRevision: String? = null,
        val electricalVersion: String? = null,
        val minorBootloader: Int? = null,
        val firmwareUpdateFileFormatVersion: Int? = null,
    )

    @Serializable
    data class ComponentPart(
        val serialNumber: String? = null,
        val hardwareVersion: HardwareVersion? = null,
        val firmwareVersion: FirmwareVersion? = null,
        val busAddress: String? = null,
    )

    @Serializable
    data class HardwareVersion(
        var hardwareComponentType: String? = null,
        var versionHardware: Char? = null,
        var productComponentType: String? = null,
        var subComponent: Char? = null,
    )
}
