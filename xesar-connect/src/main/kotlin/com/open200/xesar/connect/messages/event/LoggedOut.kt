package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to log out of the Xesar system.
 *
 * @param token The authentication token associated with the logged-out event.
 */
@Serializable class LoggedOut(val token: String) : Event
