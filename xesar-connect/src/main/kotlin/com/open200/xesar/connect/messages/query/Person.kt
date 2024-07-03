package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a person in the system.
 *
 * @param id The unique identifier of the person.
 * @param partitionId The partition identifier of the person.
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person (optional).
 * @param personalReferenceDuration The personal reference duration of the person.
 * @param disengagePeriod The disengage period of the person.
 * @param identificationMediaCount The count of identification media associated with the person
 *   (optional).
 * @param outdatedMedia Indicates if the person has outdated media (optional).
 * @param externalId The external ID of the person (optional).
 * @param external Indicates if the person is external (optional).
 */
@Serializable
data class Person(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val firstName: String,
    val lastName: String,
    val identifier: String? = null,
    val personalReferenceDuration: PersonalLog,
    val disengagePeriod: DisengagePeriod? = null,
    val identificationMediaCount: Int? = null,
    val outdatedMedia: Boolean? = null,
    val externalId: String? = null,
    val external: Boolean? = null,
    val defaultAuthorizationProfile: String? = null,
    val zones: List<@Serializable(with = UUIDSerializer::class) UUID>? = emptyList(),
    val installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID>? = emptyList(),
    @Serializable(with = UUIDSerializer::class) val defaultAuthorizationProfileId: UUID? = null
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "persons"
    }
}
