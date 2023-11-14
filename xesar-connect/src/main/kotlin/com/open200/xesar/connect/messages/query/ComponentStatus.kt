package com.open200.xesar.connect.messages.query

/** The status of the component (e.g. whether the configuration is up-to-date or not). */
enum class ComponentStatus {
    AssembledPrepared,
    Synced,
    NotSynced,
    RemovePrepared
}
