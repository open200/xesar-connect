package com.open200.xesar.connect.messages.session

import kotlinx.serialization.Serializable

/**
 * Represents a logged-in event.
 *
 * @param token The authentication token associated with the logged-in session.
 */
@Serializable data class LoggedIn(val token: String) : SessionEvent
