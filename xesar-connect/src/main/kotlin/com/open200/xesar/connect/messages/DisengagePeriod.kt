package com.open200.xesar.connect.messages

import kotlinx.serialization.Serializable

/**
 * Represents the disengage period. The values for SHORT/LONG in seconds can be changed in the
 * installation settings.
 *
 * @param SHORT The short disengage period.
 * @param LONG The long disengage period.
 */
@Serializable
enum class DisengagePeriod {
    SHORT,
    LONG,
}
