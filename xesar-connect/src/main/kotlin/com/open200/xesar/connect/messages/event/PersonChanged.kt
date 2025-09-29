package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a person information.
 *
 * @param firstName The first name of the person.
 * @param lastName The last name of the person.
 * @param identifier The identifier of the person.
 * @param defaultAuthorizationProfileId The default authorization profile id of the person.
 * @param oldDefaultAuthorizationProfileId The old default authorization profile id of the person.
 * @param oldPersonalReferenceDuration The old personal reference duration of the person.
 * @param personalReferenceDuration The personal reference duration of the person.
 * @param disengagePeriod The disengage period of the person.
 * @param id The id of the person.
 * @param entityMetadata Contains the information for all defined custom data fields for the person.
 */
@Serializable
data class PersonChanged(
    val firstName: String,
    val lastName: String,
    val identifier: String? = null,
    @Serializable(with = UUIDSerializer::class) val defaultAuthorizationProfileId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val oldDefaultAuthorizationProfileId: UUID? = null,
    val oldPersonalReferenceDuration: PersonalLog? = null,
    val personalReferenceDuration: PersonalLog? = null,
    val disengagePeriod: DisengagePeriod? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val entityMetadata: List<EntityMetadata>? = null,
) : Event
