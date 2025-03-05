package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete a person.
 *
 * @param defaultAuthorizationProfileId The default authorization profile id of the person.
 * @param id The id of the person to delete.
 * @param mediums The mediums of the person.
 */
@Serializable
data class PersonDeleted(
    val defaultAuthorizationProfileId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val mediums: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
) : Event
