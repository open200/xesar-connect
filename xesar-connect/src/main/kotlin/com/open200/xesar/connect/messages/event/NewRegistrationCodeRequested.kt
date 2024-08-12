package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO to notify that a new registration code for a smartphone media has been
 * requested.
 *
 * @param id The id of the smartphone media.
 */
@Serializable
data class NewRegistrationCodeRequested(@Serializable(with = UUIDSerializer::class) val id: UUID) :
    Event
