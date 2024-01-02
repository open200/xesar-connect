package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents the authorization data which should be added to a medium.
 *
 * @param authorizationName The name of the installationPoint or zone.
 * @param authorizationProfileId The id of the (individual) authorization profile. May be NULL for
 *   new (individual) authorization profiles.
 * @param timeProfileName The name of the time profile.
 * @param authorizationId The id of the target installation point or zone.
 * @param installationPoint Indicates whether the (individual) authorization targets an installation
 *   point (TRUE) or a zone (FALSE).
 * @param timeProfileId The id of the time profile.
 */
@Serializable
data class AuthorizationData(
    val authorizationName: String? = null,
    @Serializable(with = UUIDSerializer::class) val authorizationProfileId: UUID? = null,
    var timeProfileName: String? = null,
    @Serializable(with = UUIDSerializer::class) val authorizationId: UUID? = null,
    var installationPoint: Boolean? = null,
    @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID? = null
)
