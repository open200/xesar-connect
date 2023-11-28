package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an installation point in the system.
 *
 * @param id The unique identifier of the installation point.
 * @param name The name of the installation point.
 * @param description The description of the installation point (optional).
 * @param installationId The installation ID of the installation point.
 * @param installationType The type of the installation point (optional).
 * @param linkedInstallationPoints The list of linked installation point identifiers associated with
 *   the installation point.
 * @param onlineStatus The online status of the installation point.
 * @param componentType The component type of the installation point.
 * @param releaseDurationShort The short release duration of the installation point.
 * @param releaseDurationLong The long release duration of the installation point.
 * @param logMode The log mode of the installation point.
 * @param days The number of days for the installation point.
 * @param manualOfficeMode Indicates if manual office mode is enabled for the installation point.
 * @param shopMode Indicates if shop mode is enabled for the installation point.
 * @param openDoor Indicates if the door is open for the installation point.
 * @param bleStatus The BLE status of the installation point.
 * @param timeProfileName The name of the time profile associated with the installation point
 *   (optional).
 * @param batteryCondition The battery condition of the installation point (optional).
 */
@Serializable
data class InstallationPoint(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val description: String? = null,
    val installationId: String,
    val installationType: String? = null,
    val linkedInstallationPoints: List<@Serializable(with = UUIDSerializer::class) UUID>? =
        emptyList(),
    val onlineStatus: OnlineStatus? = null,
    val componentType: ComponentType,
    val releaseDurationShort: Int,
    val releaseDurationLong: Int,
    val logMode: String? = null,
    val days: Int? = null,
    val manualOfficeMode: Boolean,
    val shopMode: Boolean,
    val openDoor: Boolean,
    val bleStatus: String? = null,
    val timeProfileName: String? = null,
    val batteryCondition: String? = null,
) : QueryListResource, QueryElementResource {
    companion object {
        const val QUERY_RESOURCE = "installation-points"
    }
}
