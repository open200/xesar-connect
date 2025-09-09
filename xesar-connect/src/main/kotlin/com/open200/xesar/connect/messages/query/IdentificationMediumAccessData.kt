package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents the access data of an identification medium in the system.
 *
 * @param xsMediumId The unique identifier of the identification medium.
 *
 * @param validityDuration The duration of validity for the identification media (optional).

 */
@Serializable
data class IdentificationMediumAccessData(
    val identificationMedium: IdentificationMedium,
    val mediumType: MediumType? = null,
    val state: State? = null,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "identification-media-access-data"
    }

    @Serializable
    data class IdentificationMedium(
        val masterKey: Boolean,
        val mediumDataFrame: String,
        val metadata: Metadata,
        val officeMode: Boolean,
        val t: String? = null,
        @Serializable(with = UUIDSerializer::class) val transactionId: UUID? = null,
        @Serializable(with = LocalDateTimeSerializer::class) val ts: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class) val validFrom: LocalDateTime,
        @Serializable(with = LocalDateTimeSerializer::class) val validUntil: LocalDateTime,
        val version: Int,
        val xsId: String,
        val xsMOBDK: String? = null,
        val xsMOBGID: String? = null,
        @Serializable(with = UUIDSerializer::class) val xsMediumId: UUID,
        @Serializable(with = UUIDSerializer::class) val xsMobileId: UUID? = null,
        )

    @Serializable
    data class Metadata(
        val accessPoints: List<AccessPoint>? = null,
    )

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
