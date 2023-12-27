package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to change a user group.
 *
 * @param assignableAuthorizationProfiles List of assignable authorization profiles.
 * @param permissions List of permissions.
 * @param name Name of the user group.
 * @param description Description of the user group.
 * @param id id of the user group.
 */
@Serializable
data class UserGroupChanged(
    val assignableAuthorizationProfiles: List<@Serializable(with = UUIDSerializer::class) UUID> =
        emptyList(),
    val permissions: List<String> = emptyList(),
    val name: String,
    val description: String,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : Event
