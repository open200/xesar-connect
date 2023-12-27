package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.Token
import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to create an installation point.
 *
 * @param componentType The type of component installed in the installation point.
 * @param aggregateId The id of the installation point.
 * @param linkedInstallationPoints A map consisting of the installation-point id as the key and the
 *   properties as values. Use this if the installation point has more than one evva component. For
 *   example the Evva component type 'WallReader2x'.
 * @param commandId The id of the command.
 * @param properties The properties of the installation point.
 * @param token The token of the command.
 */
@Serializable
data class CreateInstallationPointMapi(
    val componentType: ComponentType,
    @Serializable(with = UUIDSerializer::class) val aggregateId: UUID,
    val linkedInstallationPoints:
        Map<@Serializable(with = UUIDSerializer::class) UUID, Properties> =
        emptyMap(),
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val properties: Properties,
    val token: Token
) : Command {
    @Serializable
    data class Properties(
        val name: String,
        val description: String? = null,
        val installationId: String,
        val installationType: String? = null
    )
}
