package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/** Represents a command POJO to set the phone number on a smartphone media. */
@Serializable
data class SetPhoneNumberOnSmartphoneMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val phoneNumber: String,
    val token: String
) : Command
