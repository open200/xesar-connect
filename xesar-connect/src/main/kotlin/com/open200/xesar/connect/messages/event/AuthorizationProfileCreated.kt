package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to create an authorization profile.
 *
 * @param individual Whether the authorization profile is individual.
 * @param partitionId The id of the partition.
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile.
 * @param id The id of the authorization profile.
 * @param assignable Whether the authorization profile is assignable.
 */
@Serializable
data class AuthorizationProfileCreated(
    val individual: Boolean,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val name: String,
    val description: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val assignable: Boolean,
) : Event
