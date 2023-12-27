package com.open200.xesar.connect.messages.command

import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to log out of the system.
 *
 * @param token The token associated with the user's session to be logged out.
 */
@Serializable data class Logout(val token: String) : Command
