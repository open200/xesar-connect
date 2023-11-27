package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents a logged-in event.
 *
 * @param token The authentication token associated with the logged-in event.
 */
@Serializable data class LoggedIn(val token: String) : Event
