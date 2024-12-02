package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a person on a medium.
 *
 * @param oldPersonId The old person id of the medium.
 * @param newPersonId The new person id of the medium.
 * @param id The id of the medium.
 */
@Serializable
data class MediumPersonChanged(
    @Serializable(with = UUIDSerializer::class) val oldPersonId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val newPersonId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID
) : Event
