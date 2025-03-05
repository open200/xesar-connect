package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.messages.BluetoothState
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to configure the bluetooth state of an installation point.
 *
 * @param commandId The unique identifier of the command.
 * @param bluetoothState The bluetooth state to configure.
 * @param id The unique identifier of the installation point.
 * @param token The token of the command.
 */
@Serializable
data class ConfigureBluetoothStateMapi(
    override val commandId: @Serializable(with = UUIDSerializer::class) UUID,
    val bluetoothState: BluetoothState,
    val id: @Serializable(with = UUIDSerializer::class) UUID,
    val token: String
) : Command
