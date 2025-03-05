package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.command.AuthorizationData
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to add a smartphone media to an installation.
 *
 * @param accessBeginAt The timestamp when access begins for the smartphone media (optional).
 * @param partitionId The identifier of the partition associated with the smartphone media
 *   (optional).
 * @param disengagePeriod The disengage period of the smartphone media (optional).
 * @param mediumIdentifier The identifier of the smartphone media.
 * @param authorizationProfileId The identifier of the authorization profile associated with the
 *   smartphone media (optional).
 * @param individualAuthorizations The list of individual authorization profile identifiers
 *   associated with the smartphone media.
 * @param sagaId The unique identifier of the saga.
 * @param messageLanguage The language for correspondence (optional).
 * @param label The label of the smartphone media (optional).
 * @param validityBeginAt The timestamp when the validity of the smartphone media begins.
 * @param accessEndAt The timestamp when access ends for the smartphone media (optional).
 * @param validityEndAt The timestamp when the validity of the smartphone media ends.
 * @param aggregateId The unique identifier of the aggregate (in this case the medium id).
 * @param phoneNumber The phone number of the smartphone media (optional).
 * @param validityDuration The duration of validity for the smartphone media (optional).
 * @param personId The unique identifier of the person associated with the smartphone media
 *   (optional).
 * @param issuedAt The timestamp when the smartphone media was issued.
 */
@Serializable
data class SmartphoneAddedToInstallation(
    @Serializable(with = LocalDateTimeSerializer::class) val accessBeginAt: LocalDateTime? = null,
    val partitionId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val disengagePeriod: DisengagePeriod? = null,
    val mediumIdentifier: Long? = null,
    @Serializable(with = UUIDSerializer::class) val authorizationProfileId: UUID? = null,
    val individualAuthorizations: List<AuthorizationData> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val sagaId: UUID? = null,
    val messageLanguage: String? = null,
    val label: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val validityBeginAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val accessEndAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val validityEndAt: LocalDateTime? = null,
    @Serializable(with = UUIDSerializer::class) val aggregateId: UUID? = null,
    val phoneNumber: String? = null,
    val validityDuration: Short? = null,
    @Serializable(with = UUIDSerializer::class) val personId: UUID? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val issuedAt: LocalDateTime? = null,
) : Event
