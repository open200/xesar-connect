package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a medium.
 *
 * @param accessBeginAt The access begin date of the medium.
 * @param disengagePeriod The disengage period of the medium.
 * @param authorizationProfileId The authorization profile id of the medium.
 * @param validityDuration The validity duration of the medium.
 * @param validUntil The valid until date of the medium.
 * @param changedAt The changed at date of the medium.
 * @param id The id of the medium.
 * @param label The label of the medium.
 * @param validFrom The valid from date of the medium.
 * @param accessEndAt The access end date of the medium.
 * @param phoneNumber The phone number of the medium (smartphone).
 * @param messageLanguage The message language of the medium (smartphone).
 */
@Serializable
data class MediumChanged(
    val accessBeginAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val disengagePeriod: DisengagePeriod? = null,
    val authorizationProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val validityDuration: Short? = null,
    val validUntil: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val changedAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val label: String? = null,
    val validFrom: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val accessEndAt: @Serializable(with = LocalDateTimeSerializer::class) LocalDateTime? = null,
    val phoneNumber: String? = null,
    val messageLanguage: String? = null,
) : Event
