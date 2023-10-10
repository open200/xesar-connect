package com.open200.xesar.connect.messages.command

import kotlinx.serialization.Serializable

/**
 * Represents a logout command in the system.
 *
 * @param token The token associated with the user's session to be logged out.
 */
@Serializable data class Logout(val token: String) : Command
