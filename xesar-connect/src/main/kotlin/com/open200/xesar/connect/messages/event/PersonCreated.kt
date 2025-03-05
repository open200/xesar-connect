package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create a person.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param defaultAuthorizationProfileId The default authorization profile id of the person.
 * @param partitionId The partition id of the person.
 * @param personalReferenceDuration The personal reference duration of the person.
 * @param disengagePeriod The disengage period of the person.
 * @param externalId The external id of the person.
 * @param id The id of the person.
 */
@Serializable
data class PersonCreated(
    val firstName: String,
    val lastName: String,
    val identifier: String? = null,
    @Serializable(with = UUIDSerializer::class) val defaultAuthorizationProfileId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val personalReferenceDuration: PersonalLog,
    val disengagePeriod: DisengagePeriod? = null,
    val externalId: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : Event
