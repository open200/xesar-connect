package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change the information of an authorization
 * profile.
 *
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile.
 * @param id The id of the authorization profile.
 * @param entityMetadata Contains the information for all defined custom data fields for the
 *   authorization profile.
 */
@Serializable
data class AuthorizationProfileInfoChanged(
    val name: String? = null,
    val description: String? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val entityMetadata: List<EntityMetadata>? = null,
) : Event
