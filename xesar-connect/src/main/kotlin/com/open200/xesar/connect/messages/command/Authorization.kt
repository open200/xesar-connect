package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a POJO for authorizations.
 *
 * @param id The id of the authorization.
 * @param timeProfileId The id of the time profile.
 */
@Serializable
data class Authorization(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID? = null,
)
