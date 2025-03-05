package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.*
import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create an installation point.
 *
 * @param componentType The type of the component.
 * @param releaseDurationShort The short release duration of the installation point.
 * @param instance The instance of the installation point.
 * @param upgradeMedia The upgrade media flag of the installation point.
 * @param partitionId The partition id of the installation point.
 * @param personalReferenceDuration The personal reference duration of the installation point.
 * @param timeProfileName The name of the time profile of the installation point.
 * @param description The description of the installation point.
 * @param nextDstTransition The next DST transition of the installation point.
 * @param linkedInstallationPoints The list of linked installation points of the installation point.
 * @param timeProfileId The id of the time profile of the installation point.
 * @param accessId The access id of the installation point.
 * @param shopMode The shop mode flag of the installation point.
 * @param releaseDurationLong The long release duration of the installation point.
 * @param installationType The type of the installation point.
 * @param manualOfficeMode The manual office mode flag of the installation point.
 * @param name The name of the installation point.
 * @param afterNextDstTransition The after next DST transition of the installation point.
 * @param evvaComponentId The id of the EVVA component of the installation point.
 * @param id The id of the installation point.
 * @param installationId The installation id of the installation point.
 * @param timeProfileData The time profile data of the installation point.
 * @param calendarData The calendar data of the installation point.
 * @param openDoor The open door flag of the installation point.
 */
@Serializable
data class InstallationPointCreated(
    val componentType: ComponentType? = null,
    val releaseDurationShort: Int? = null,
    val instance: Int? = null,
    val upgradeMedia: Boolean? = null,
    val partitionId: String? = null,
    val personalReferenceDuration: PersonalLog? = null,
    val timeProfileName: String? = null,
    val description: String? = null,
    val nextDstTransition: String? = null,
    val linkedInstallationPoints: List<LinkedInstallationPoint>? = emptyList(),
    val timeProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val accessId: Long? = null,
    val shopMode: Boolean? = null,
    val releaseDurationLong: Int? = null,
    val installationType: String? = null,
    val manualOfficeMode: Boolean? = null,
    val name: String? = null,
    val afterNextDstTransition: String? = null,
    val evvaComponentId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val installationId: String? = null,
    val timeProfileData: TimeProfileData? = null,
    val calendarData: CalendarData? = null,
    val openDoor: Boolean? = null,
) : Event {

    @Serializable
    data class TimeProfileData(
        var timeSeries: List<TimeSerie>? = null,
        var exceptionTimeSeries: List<ExceptionTimeSerie>? = emptyList(),
        var timePointSeries: List<TimePointSerie>? = emptyList(),
        var exceptionTimePointSeries: List<ExceptionTimepointSerie>? = emptyList(),
    )

    @Serializable
    data class LinkedInstallationPoint(
        val aggregateId: @Serializable(with = UUIDSerializer::class) UUID? = null,
        val accessId: Long? = null,
        val installationPointProperties: InstallationPointProperties? = null,
        val evvaComponentId: @Serializable(with = UUIDSerializer::class) UUID? = null,
        val componentType: ComponentType? = null,
    ) {
        @Serializable
        data class InstallationPointProperties(
            var name: String,
            var description: String? = null,
            var partitionId: @Serializable(with = UUIDSerializer::class) UUID,
            var releaseDurationShort: Int? = null,
            var releaseDurationLong: Int? = null,
            var installationId: String,
            var installationType: String? = null,
            var personalReferenceDuration: PersonalLog,
            var timeProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
            var manualOfficeMode: Boolean? = null,
            var shopMode: Boolean? = null,
            var openDoor: Boolean? = null,
            var upgradeMedia: Boolean? = null,
        )
    }

    @Serializable
    data class CalendarData(
        var calendars: Map<Int, List<@Serializable(with = LocalDateSerializer::class) LocalDate>>? =
            null
    )
}
