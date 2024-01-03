package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to log into the system.
 *
 * @param commandId The unique identifier for the login command.
 * @param username The username for the login.
 * @param password The password for the login.
 */
@Serializable
data class Login(
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val username: String,
    val password: String
) : Command
