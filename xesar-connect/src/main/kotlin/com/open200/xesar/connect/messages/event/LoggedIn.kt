package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to log in to the Xesar system.
 *
 * @param token The authentication token associated with the logged-in event.
 */
@Serializable data class LoggedIn(val token: String) : Event
