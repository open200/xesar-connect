package com.open200.xesar.connect.messages.event

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
 *   this partition
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
) : Event
