package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to unregister a smartphone media.
 *
 * @param id The id of the smartphone media.
 */
@Serializable
data class SmartphoneUnregistered(
    @Serializable(with = UUIDSerializer::class) val id: UUID? = null
) : Event
