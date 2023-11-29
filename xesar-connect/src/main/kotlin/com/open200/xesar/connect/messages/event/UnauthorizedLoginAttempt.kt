package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents a login attempt with wrong credentials.
 *
 * @param username The username of the unauthorized login attempt.
 * @param channel The channel of the unauthorized login attempt.
 * @param ipAddress The IP address of the client that sent the request.
 */
@Serializable
data class UnauthorizedLoginAttempt(
    val username: String,
    val channel: Channel,
    val ipAddress: String? = null
) : Event {
    enum class Channel {
        API,
        WEB,
        MAINTENANCE_COMPONENT
    }
}
