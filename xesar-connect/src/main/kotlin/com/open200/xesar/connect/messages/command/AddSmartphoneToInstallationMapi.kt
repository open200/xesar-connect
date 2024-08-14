package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Adds a smartphone media to an installation.
 *
 * @param commandId The unique identifier of the command.
 * @param accessBeginAt The timestamp when access begins for the smartphone media (optional).
 * @param partitionId The identifier of the partition associated with the smartphone media
 *   (optional).
 * @param disengagePeriod The disengage period of the smartphone media (optional).
 * @param authorizationProfileId The identifier of the authorization profile associated with the
 *   smartphone media (optional).
 * @param individualAuthorizations The individual authorizations associated with the smartphone
 *   media.
 * @param messageLanguage The language for correspondence (optional).
 * @param label The label of the smartphone media (optional).
 * @param accessEndAt The timestamp when access ends for the smartphone media (optional).
 * @param phoneNumber The phone number of the smartphone media (optional).
 * @param validityDuration The duration of validity for the smartphone media (optional).
 * @param personId The unique identifier of the person associated with the smartphone media
 *   (optional).
 * @param id The unique identifier of the smartphone media.
 * @param token The token of the command.
 */
@Serializable
data class AddSmartphoneToInstallationMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val accessBeginAt: LocalDateTime? = null,
    val partitionId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val disengagePeriod: DisengagePeriod? = null,
    @Serializable(with = UUIDSerializer::class) val authorizationProfileId: UUID? = null,
    val individualAuthorizations: List<AuthorizationData> = emptyList(),
    val messageLanguage: String? = null,
    val label: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val accessEndAt: LocalDateTime? = null,
    val phoneNumber: String? = null,
    val validityDuration: Short? = null,
    @Serializable(with = UUIDSerializer::class) val personId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
