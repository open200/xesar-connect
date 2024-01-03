package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.command.AuthorizationData
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to add individual authorizations to a medium.
 *
 * @param id The id of the medium.
 * @param authorizationProfiles The list of authorization profiles that were added to the medium.
 */
@Serializable
data class IndividualAuthorizationsAddedToMedium(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val authorizationProfiles: List<AuthorizationData> = emptyList()
) : Event
