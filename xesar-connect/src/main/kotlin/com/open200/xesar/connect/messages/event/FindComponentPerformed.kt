package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * This event is sent back to the client when the FindComponent command was performed.
 *
 * @param ok The result of the command.
 */
@Serializable data class FindComponentPerformed(val ok: String) : Event
