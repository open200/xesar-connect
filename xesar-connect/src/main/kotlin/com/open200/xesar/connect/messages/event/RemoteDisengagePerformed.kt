package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents a remote disengage event.
 *
 * @param ok The result of the command.
 */
@Serializable data class RemoteDisengagePerformed(val ok: String) : Event
