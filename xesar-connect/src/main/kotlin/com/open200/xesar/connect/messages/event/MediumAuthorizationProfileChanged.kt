package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to assign an individual authorization to a
 * medium.
 *
 * @param id The id of the medium.
 * @param newAuthorizationProfileId The id of the authorization profile that was added to the
 *   medium.
 * @param oldAuthorizationProfileId The id of the authorization profile that was removed from the
 *   medium.
 */
@Serializable
data class MediumAuthorizationProfileChanged(
    var id: @Serializable(with = UUIDSerializer::class) UUID,
    var newAuthorizationProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    var oldAuthorizationProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null
) : Event
