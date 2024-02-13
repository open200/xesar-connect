package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO as a response of a command to add a medium to an installation.
 *
 * @param aggregateId The id of the medium.
 * @param partitionId The id of the partition.
 * @param hardwareId The hardware id of the medium.
 * @param mediumIdentifier The medium identifier.
 * @param terminalId The id of the terminal.
 * @param label The label of the medium.
 */
@Serializable
data class AddMediumToInstallationRequested(
    @Serializable(with = UUIDSerializer::class) val aggregateId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID? = null,
    val hardwareId: String? = null,
    val mediumIdentifier: Long? = null,
    @Serializable(with = UUIDSerializer::class) val terminalId: UUID? = null,
    val label: String? = null,
) : Event
