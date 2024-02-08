package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to add a medium to an installation.
 *
 * @param aggregateId The id of the installation.
 * @param hardwareId The hardware ID of the installation.
 * @param nativeId The native ID of the installation.
 * @param mediumIdentifier The identifier of the medium.
 * @param label The label of the medium.
 */
@Serializable
data class MediumAddedToInstallation(
    var aggregateId: @Serializable(with = UUIDSerializer::class) UUID? = null,
    var hardwareId: String? = null,
    var nativeId: String? = null,
    var mediumIdentifier: Long? = null,
    var label: String? = null
) : Event
