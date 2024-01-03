package com.open200.xesar.connect.messages.event

import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to find a component.
 *
 * @param ok The result of the command.
 */
@Serializable data class FindComponentPerformed(val ok: String) : Event
