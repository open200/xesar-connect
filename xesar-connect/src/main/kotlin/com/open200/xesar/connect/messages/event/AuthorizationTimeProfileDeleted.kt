package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete an authorization time profile.
 *
 * @param id The id of the authorization time profile.
 */
@Serializable
data class AuthorizationTimeProfileDeleted(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : Event
