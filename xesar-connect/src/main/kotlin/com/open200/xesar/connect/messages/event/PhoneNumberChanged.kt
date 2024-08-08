package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/** Represents an event POJO as a response of a command when a phone number was changed. */
@Serializable
data class PhoneNumberChanged(
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val phoneNumber: String? = null
) : Event
