package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete individual authorizations.
 *
 * @param individualAuthorizations The list of individual authorizations that were deleted.
 * @param mediumId The id of the medium where the individual authorizations were deleted.
 */
@Serializable
data class IndividualAuthorizationsDeleted(
    val individualAuthorizations: List<@Serializable(with = UUIDSerializer::class) UUID>,
    val mediumId: @Serializable(with = UUIDSerializer::class) UUID
) : Event
