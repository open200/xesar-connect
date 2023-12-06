package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents a remote disengage permanent event.
 *
 * @param ok The result of the command.
 */
@Serializable data class RemoteDisengagePermanentPerformed(val ok: String) : Event
