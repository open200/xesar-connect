package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.OffsetDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.OffsetDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents the access data of an identification medium in the system.
 *
 * @param identificationMedium Container for medium metadata and access data.
 * @param mediumType The type of the identification media.
 * @param state The state of the "Self Service" transaction. Only present if the installation is in "Self Service" mode and the medium is a smartphone (optional).
 *
 */
@Serializable
data class IdentificationMediumAccessData(
    val identificationMedium: IdentificationMedium,
    val mediumType: MediumType,
    val state: State? = null,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "identification-media-access-data"
    }

/**
 * Represents the access data of an identification medium in the system.
 *
    * @param masterKey Whether the medium is a master key.
    * @param mediumDataFrame The hex encoded access data for the medium. If the medium type is Smartphone, this value is also encrypted.
    * @param metadata Container for extra descriptive data.
    * @param officeMode If this medium is allowed to toggle office mode.
    * @param t The document type (optional).
    * @param transactionId The unique identifier for the transaction (optional).
    * @param ts The ISO8601 timestamp of when this resource was requested.
    * @param validFrom The ISO8601 time from when the medium is valid.
    * @param validUntil he ISO8601 time up until the medium is valid.
    * @param version The version of this specification.
    * @param xsId A SHA256 hash of the installation ID.
    * @param xsMOBDK The Mobile Device Key (MOBDK). Only present for Smartphone media (optional).
    * @param xsMOBGID The Mobile Group Identifier (MOBGID). Only present for Smartphone media (optional).
    * @param xsMediumId The unique identifier of the identification medium.
    * @param xsMobileId The unique identifier of the Smartphone, this is not the same as the medium ID. Only present for Smartphone media (optional).
 */
    @Serializable
    data class IdentificationMedium(
        val masterKey: Boolean,
        val mediumDataFrame: String,
        val metadata: Metadata,
        val officeMode: Boolean,
        val t: String? = null,
        @Serializable(with = UUIDSerializer::class) val transactionId: UUID? = null,
        @Serializable(with = OffsetDateTimeSerializer::class) val ts: OffsetDateTime,
        @Serializable(with = OffsetDateTimeSerializer::class) val validFrom: OffsetDateTime,
        @Serializable(with = OffsetDateTimeSerializer::class) val validUntil: OffsetDateTime,
        val version: Int,
        val xsId: String,
        val xsMOBDK: String? = null,
        val xsMOBGID: String? = null,
        @Serializable(with = UUIDSerializer::class) val xsMediumId: UUID,
        @Serializable(with = UUIDSerializer::class) val xsMobileId: UUID? = null,
        )

    /**
     * Represents the container for extra descriptive data.
     *
     * @param accessPoints The list of access points the medium is authorized to disengage (optional).
     */
    @Serializable
    data class Metadata(
        val accessPoints: List<AccessPoint>? = null,
    )

    /**
     * Represents an access point.
     *
     * @param accessDescription Description of the access point (optional).
     * @param bleMac The MAC address of the EVVA component (optional).
     * @param name The name of the EVVA component.
     */
    @Serializable
    data class AccessPoint(
        val accessDescription: String? = null,
        val bleMac: String? = null,
        val name: String,
    )

    enum class MediumType {
        SMARTPHONE,
        PASSIVE,
    }

    enum class State {
        UPDATE_PENDING,
        UPDATE_COMPLETE,
        REVOKE_PENDING,
        REVOKE_COMPLETE,
        LOCKED,
    }
}
