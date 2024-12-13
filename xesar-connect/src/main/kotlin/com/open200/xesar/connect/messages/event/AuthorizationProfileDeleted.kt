package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete an authorization profile.
 *
 * @param installationPoints The corresponding installation points of the authorization profile.
 * @param individual Whether the authorization profile is individual.
 * @param timeProfiles The corresponding time profiles of the authorization profile.
 * @param id The id of the authorization profile.
 * @param zones The corresponding zones of the authorization profile.
 * @param standardTimeProfile The standard time profile of the authorization profile.
 */
@Serializable
data class AuthorizationProfileDeleted(
    val installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    val individual: Boolean,
    val timeProfiles: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val zones: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val standardTimeProfile: UUID? = null
) : Event
