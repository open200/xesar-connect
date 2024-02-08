package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to withdraw an authorization profile from a medium. Withdraw means that
 * we don't have the physical medium in our hands. We remove the authorizations. The next time the
 * medium is updated, authorizations will be removed from the physical card. Most likely this will
 * happen at an online-wallreader.
 *
 * @param commandId The id of the command.
 * @param authorizationProfileId The id of the authorization profile.
 * @param id The id of the medium.
 * @param token The token of the command.
 */
@Serializable
data class WithdrawAuthorizationProfileFromMediumMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    @Serializable(with = UUIDSerializer::class) val authorizationProfileId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val token: String
) : Command
