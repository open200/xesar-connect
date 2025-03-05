package com.open200.xesar.connect.messages.query

/**
 * Represents the condition of the battery ('Undefined' if evva component has no battery or value is
 * unknown at the moment).
 */
enum class BatteryCondition {
    Full,
    AlmostEmpty,
    Undefined,
}
