package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an identification media in the system.
 *
 * @param id The unique identifier of the identification media.
 * @param label The label of the identification media.
 * @param issuedAt The timestamp when the identification media was issued.
 * @param syncedAt The timestamp when the identification media was last synced.
 * @param validityDuration The duration of validity for the identification media (optional).
 * @param authorizationProfileId The identifier of the authorization profile associated with the
 *   identification media (optional).
 * @param authorizationProfileName The name of the authorization profile associated with the
 *   identification media (optional).
 * @param individualAuthorizationProfileIds The list of individual authorization profile identifiers
 *   associated with the identification media.
 * @param mediumState The state of the identification media.
 * @param accessBeginAt The timestamp when access begins for the identification media.
 * @param accessEndAt The timestamp when access ends for the identification media (optional).
 * @param validityBeginAt The timestamp when the validity of the identification media begins.
 * @param validityEndAt The timestamp when the validity of the identification media ends.
 * @param validityBeginAtInHardware The timestamp when the validity of the identification media
 *   begins in the hardware.
 * @param validityEndAtInHardware The timestamp when the validity of the identification media ends
 *   in the hardware.
 * @param external Indicates if the identification media is external.
 * @param disengagePeriod The disengage period of the identification media.
 * @param mediumIdentifier The identifier of the identification media.
 * @param outdated Indicates if the identification media is outdated.
 * @param personId The unique identifier of the person associated with the identification media
 *   (optional).
 * @param person The name of the person associated with the identification media (optional).
 * @param hardwareId The hardware ID of the identification media.
 * @param nativeId The native ID of the identification media.
 * @param secure Indicates if the identification media is secure.
 * @param softwareStatus The software status of the identification media.
 * @param hardwareStatus The hardware status of the identification media.
 * @param fitsOnHardware Indicates if the identification media fits on the hardware.
 * @param userId The unique identifier of the user associated with the identification media.
 * @param userName The name of the user associated with the identification media.
 * @param requiredAction The required action for the identification media.
 * @param mediumType The type of the identification media (optional).
 */
@Serializable
data class IdentificationMedium(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val label: String,
    @Serializable(with = LocalDateTimeSerializer::class) val issuedAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val syncedAt: LocalDateTime? = null,
    val validityDuration: Int? = null,
    @Serializable(with = UUIDSerializer::class) val authorizationProfileId: UUID? = null,
    val authorizationProfileName: String? = null,
    val individualAuthorizationProfileIds: List<@Serializable(with = UUIDSerializer::class) UUID>,
    val mediumState: String,
    @Serializable(with = LocalDateTimeSerializer::class) val accessBeginAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val accessEndAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val validityBeginAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val validityEndAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val validityBeginAtInHardware: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class)
    val validityEndAtInHardware: LocalDateTime? = null,
    val external: Boolean,
    val disengagePeriod: DisengagePeriod? = null,
    val mediumIdentifier: Int,
    val outdated: Boolean,
    @Serializable(with = UUIDSerializer::class) val personId: UUID? = null,
    val person: String? = null,
    val hardwareId: String? = null,
    val nativeId: String? = null,
    val secure: Boolean,
    val softwareStatus: String,
    val hardwareStatus: String,
    val fitsOnHardware: Boolean,
    @Serializable(with = UUIDSerializer::class) val userId: UUID? = null,
    val userName: String? = null,
    val requiredAction: String,
    // TODO: mediumType is not in the documentation
    val mediumType: String? = null
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "identification-media"
    }
}
