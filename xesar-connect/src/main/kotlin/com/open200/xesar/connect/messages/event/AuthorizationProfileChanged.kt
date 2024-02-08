package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command when the data of an authorization profile has
 * changed.
 *
 * @param id The id of the authorization profile.
 */
@Serializable
data class AuthorizationProfileChanged(
    val id: @Serializable(with = UUIDSerializer::class) UUID? = null
) : Event
