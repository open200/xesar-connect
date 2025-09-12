package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.MobileServiceMode
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a partition.
 *
 * @param installationPointDefaultPersonalReferenceDuration The default personal reference duration.
 * @param replacementMediumDuration The replacement medium duration.
 * @param storeDataOnTablet Whether the data should be stored on the tablet.
 * @param mqttServerAddress The MQTT server address.
 * @param tokenValidityDurationMinutes The token validity duration in minutes.
 * @param validityDuration The validity duration.
 * @param validityThreshold The validity threshold.
 * @param personDefaultPersonalReferenceDuration The default personal reference duration.
 * @param id The ID of the partition.
 * @param dailySchedulerExecutionTime The daily scheduler execution time.
 * @param smartphoneValidityDuration The default smartphone validity duration for all smartphones of
 *   this partition.
 * @param smartphoneValidityThreshold The smartphone validity threshold.
 * @param entityMetadataDefinitions Contains the information for all defined custom data fields.
 * @param maximumEntityMetadataDefinitions The maximum allowed custom data fields per entity type.
 * @param mobileServiceMode The Mobile Service mode. Used to describe the application's method of
 *   communication with Smartphones.
 */
@Serializable
data class PartitionChanged(
    val installationPointDefaultPersonalReferenceDuration: PersonalLog? = null,
    val replacementMediumDuration: Short? = null,
    val storeDataOnTablet: Boolean? = null,
    val mqttServerAddress: String? = null,
    val tokenValidityDurationMinutes: Int? = null,
    val validityDuration: Short? = null,
    val validityThreshold: Short? = null,
    val personDefaultPersonalReferenceDuration: PersonalLog? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val dailySchedulerExecutionTime: @Serializable(with = LocalTimeSerializer::class) LocalTime? =
        null,
    val smartphoneValidityDuration: Short? = null,
    val smartphoneValidityThreshold: Short? = null,
    val entityMetadataDefinitions: EntityMetadataDefinitions? = null,
    val maximumEntityMetadataDefinitions: Short? = null,
    val mobileServiceMode: MobileServiceMode? = null,
) : Event {

    /**
     * Represents the container for all entity types and their respective defined custom data
     * fields.
     *
     * @param authorizationProfiles The custom metadata that can be set for each authorization
     *   profile entity.
     * @param identificationMedia The custom metadata that can be set for each identification medium
     *   entity.
     * @param installationPoints The custom metadata that can be set for each installation point
     *   entity.
     * @param persons The custom metadata that can be set for each person entity.
     * @param zones The custom metadata that can be set for each zone entity.
     */
    @Serializable
    data class EntityMetadataDefinitions(
        val authorizationProfiles: List<EntityMetadata>,
        val identificationMedia: List<EntityMetadata>,
        val installationPoints: List<EntityMetadata>,
        val persons: List<EntityMetadata>,
        val zones: List<EntityMetadata>,
    )
}
