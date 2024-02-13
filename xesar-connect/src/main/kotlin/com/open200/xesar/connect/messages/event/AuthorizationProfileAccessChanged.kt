package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.command.Authorization
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command when the data of an authorization profile has
 * changed.
 *
 * @param id The id of the authorization profile.
 * @param manualOfficeMode The manual office mode.
 * @param standardTimeProfile The used standard time profile id or null for an all-day time profile.
 * @param oldStandardTimeProfile The previous standard time profile id or null for an all-day time
 *   profile.
 * @param individual The individual.
 * @param addedInstallationPoints The added installation points.
 * @param removedInstallationPoints The removed installation points.
 * @param addedZones The added zones.
 * @param removedZones The removed zones.
 * @param installationPoints The installation points.
 * @param zones The zones.
 * @param addedTimeProfiles The added time profiles.
 * @param removedTimeProfiles The removed time profiles.
 * @param media The media.
 */
@Serializable
data class AuthorizationProfileAccessChanged(
    var id: @Serializable(with = UUIDSerializer::class) UUID? = null,
    var manualOfficeMode: Boolean? = null,
    var standardTimeProfile: @Serializable(with = UUIDSerializer::class) UUID? = null,
    var oldStandardTimeProfile: @Serializable(with = UUIDSerializer::class) UUID? = null,
    var individual: Boolean? = null,
    var addedInstallationPoints: List<Authorization> = emptyList(),
    var removedInstallationPoints: List<Authorization> = emptyList(),
    var addedZones: List<Authorization> = emptyList(),
    var removedZones: List<Authorization> = emptyList(),
    var installationPoints: List<Authorization> = emptyList(),
    var zones: List<Authorization> = emptyList(),
    var addedTimeProfiles: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    var removedTimeProfiles: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    var media: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList()
) : Event
