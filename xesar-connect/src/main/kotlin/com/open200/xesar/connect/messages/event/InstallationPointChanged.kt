package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.query.TimeProfile
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change an installation point.
 *
 * @param releaseDurationShort The short release duration in seconds.
 * @param upgradeMedia Whether the media should be upgraded.
 * @param personalReferenceDurationOld The old personal reference duration.
 * @param personalReferenceDuration The new personal reference duration.
 * @param timeProfileName The name of the time profile.
 * @param description The description of the installation point.
 * @param linkedInstallationPoints The linked installation points.
 * @param timeProfileId The ID of the time profile.
 * @param shopMode Whether the shop mode is enabled.
 * @param aggregateId The id of the installation point.
 * @param releaseDurationLong The release duration (long) in seconds.
 * @param installationType The installation type.
 * @param manualOfficeMode Whether the manual office mode is enabled.
 * @param name The name of the installation point.
 * @param installationId The ID of the installation.
 * @param timeProfileData The time profile data.
 * @param openDoor Whether the door is open.
 * @param bluetoothState The Bluetooth state of the installation point (optional).
 */
@Serializable
data class InstallationPointChanged(
    val releaseDurationShort: Int? = null,
    val upgradeMedia: Boolean? = null,
    val personalReferenceDurationOld: PersonalLog? = null,
    val personalReferenceDuration: PersonalLog? = null,
    val timeProfileName: String? = null,
    val description: String? = null,
    val linkedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
    @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID? = null,
    val shopMode: Boolean? = null,
    @Serializable(with = UUIDSerializer::class) val aggregateId: UUID,
    val releaseDurationLong: Int? = null,
    val installationType: String? = null,
    val manualOfficeMode: Boolean? = null,
    val name: String? = null,
    val installationId: String? = null,
    val timeProfileData: TimeProfile? = null,
    val openDoor: Boolean? = null,
    val bluetoothState: String? = null,
) : Event
