package com.open200.xesar.connect.messages.session

import kotlinx.serialization.Serializable

/**
 * Represents an unauthorized login attempt event.
 *
 * @param event The event details of the unauthorized login attempt.
 */
@Serializable
data class UnauthorizedLoginAttempt(val event: Event) {
    /**
     * Represents the event details of the unauthorized login attempt.
     *
     * @param username The username associated with the unauthorized login attempt.
     * @param channel The channel on which the unauthorized login attempt occurred.
     */
    @Serializable data class Event(val username: String, val channel: String)
}
