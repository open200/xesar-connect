package com.open200.xesar.connect.messages.query

import QueryListResource
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a user.
 *
 * @param id The unique identifier of the user.
 * @param name The name of the user.
 * @param active Indicates whether the user is active or not.
 * @param description The description of the user (optional).
 * @param lastActive The timestamp of the user's last activity (optional).
 * @param lastLogin The timestamp of the user's last login (optional).
 * @param loginIp The IP address used for the user's login (optional).
 * @param loginType The type of login used by the user (optional).
 */
@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val active: Boolean,
    val description: String? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val lastActive: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val lastLogin: LocalDateTime? = null,
    val loginIp: String? = null,
    val loginType: String? = null,
) : QueryListResource {
    companion object {
        const val QUERY_RESOURCE = "users"
    }
}
