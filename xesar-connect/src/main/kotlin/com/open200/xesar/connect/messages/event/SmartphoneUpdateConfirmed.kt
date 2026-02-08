package com.open200.xesar.connect.messages.event

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an event POJO indicating a smartphone update was confirmed in Self Service Mode.
 *
 * @param mediumId The id of the smartphone medium.
 * @param transactionId The id of the transaction.
 */
@Serializable
data class SmartphoneUpdateConfirmed(
    @Serializable(with = UUIDSerializer::class) val mediumId: UUID,
    @Serializable(with = UUIDSerializer::class) val transactionId: UUID,
) : Event
