package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
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
 * @param identificationMediaCount The count of identification media associated with the person.
 * @param outdatedMedia Indicates if the person has outdated media.
 * @param externalId The external ID of the person (optional).
 * @param external Indicates if the person is external.
 */
@Serializable
data class Person(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val identifier: String? = null,
    val personalReferenceDuration: PersonalReferenceDuration? = null,
    val disengagePeriod: String? = null,
    val identificationMediaCount: Int? = null,
    val outdatedMedia: Boolean? = null,
    val externalId: String? = null,
    val external: Boolean? = null,
) : QueryListResource, QueryElementResource {
    /**
     * Represents the personal reference duration of the person.
     *
     * @param logMode The log mode of the personal reference duration.
     * @param days The number of days for the personal reference duration.
     */
    @Serializable
    data class PersonalReferenceDuration(val logMode: String? = null, val days: Int? = null)

    companion object {
        const val QUERY_RESOURCE = "persons"
    }
}
