package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to delete an office mode time profile.
 *
 * @param id The id of the office mode time profile.
 */
@Serializable
data class OfficeModeTimeProfileDeleted(val id: @Serializable(with = UUIDSerializer::class) UUID) :
    Event
