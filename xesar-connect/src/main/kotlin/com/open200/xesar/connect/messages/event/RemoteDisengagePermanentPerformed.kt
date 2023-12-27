package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to remotely disengage an online component
 * permanently.
 *
 * @param ok The result of the command.
 */
@Serializable data class RemoteDisengagePermanentPerformed(val ok: String) : Event
