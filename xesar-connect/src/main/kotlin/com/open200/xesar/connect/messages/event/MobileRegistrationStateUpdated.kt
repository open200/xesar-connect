package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO indicating the mobile registration state was updated.
 *
 * @param id The id of the smartphone medium.
 * @param registrationState The new registration state of the smartphone medium.
 */
@Serializable
data class MobileRegistrationStateUpdated(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val registrationState: String? = null,
) : Event
