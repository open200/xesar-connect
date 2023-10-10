package com.open200.xesar.connect.messages.session

import kotlinx.serialization.Serializable

/**
 * Represents a logged-out event.
 *
 * @param event The event details of the logged-out session.
 */
@Serializable
class LoggedOut(val event: Event) {
    /**
     * Represents the event details of the logged-out session.
     *
     * @param token The authentication token associated with the session.
     */
    @Serializable data class Event(val token: String)
}
