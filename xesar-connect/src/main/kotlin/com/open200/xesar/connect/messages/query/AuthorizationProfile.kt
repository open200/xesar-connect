package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an authorization profile in the system.
 *
 * @param id The unique identifier of the authorization profile.
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile (optional).
 * @param installationPoints The list of installation points associated with the authorization
 *   profile.
 * @param zones The list of zones associated with the authorization profile.
 * @param manualOfficeMode Indicates if manual office mode is enabled for the authorization profile.
 * @param anyAuthorizations Indicates if the authorization profile allows any authorizations.
 * @param standardTimeProfile The used standard time profile id or null for an all-day time profile.
 *   (optional).
 */
@Serializable
data class AuthorizationProfile(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val description: String? = null,
    val installationPoints: List<InstallationPoint>,
    val zones: List<Zone>,
    val manualOfficeMode: Boolean,
    val anyAuthorizations: Boolean,
    @Serializable(with = UUIDSerializer::class) val standardTimeProfile: UUID? = null,
) : QueryListResource, QueryElementResource {

    /**
     * Represents an installation point associated with an authorization profile.
     *
     * @param id The unique identifier of the installation point.
     * @param timeProfileId The time profile identifier associated with the installation point
     *   (optional).
     */
    @Serializable
    data class InstallationPoint(
        @Serializable(with = UUIDSerializer::class) val id: UUID? = null,
        @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID? = null,
    )
    /**
     * Represents a zone associated with an authorization profile.
     *
     * @param id The unique identifier of the zone.
     * @param timeProfileId The time profile identifier associated with the zone (optional).
     */
    @Serializable
    data class Zone(
        @Serializable(with = UUIDSerializer::class) val id: UUID,
        @Serializable(with = UUIDSerializer::class) val timeProfileId: UUID? = null
    )

    companion object {
        const val QUERY_RESOURCE = "authorization-profiles"
    }
}
