package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents a logged-out event.
 *
 * @param token The authentication token associated with the logged-out event.
 */
@Serializable class LoggedOut(val token: String) : Event
