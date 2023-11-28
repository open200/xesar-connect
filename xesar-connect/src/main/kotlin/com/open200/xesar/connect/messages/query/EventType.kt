package com.open200.xesar.connect.messages.query

import kotlinx.serialization.Serializable

/** Represents the type of event. */
@Serializable
enum class EventType {
    NORMAL_OPENING,
    EMERGENCY_OPENING,
    MANUAL_OPENING_STARTED,
    MANUAL_OPENING_FINISHED,
    MEDIA_RESTRICTED,
    MEDIUM_CHANGED,
    PROLONGED_NORMAL_OPENING,
    PROLONGED_MASTER_KEY_OPENING,
    OPENING_MASTER_KEY_IGNORED_BLACKLIST,
    OPENING_MASTER_KEY_PROLONGED_IGNORED_BLACKLIST,
    OPENING_NORMAL_BLACKLISTED,
    OPENING_EMERGENCY_BLACKLISTED,
    MANUAL_OPENING_STARTED_BLACKLISTED,
    OPENING_NORMAL_PROLONGED_BLACKLISTED,
    OPENING_EMERGENCY_PROLONGED_BLACKLISTED,
    OPENING_EMERGENCY_INVALID_BLACKLIST_OR_TIME_BLACKLISTED,
    OPENING_EMERGENCY_PROLONGED_INVALID_BLACKLIST_OR_TIME_BLACKLISTED,
    OPENING_NORMAL_SWITCH,
    OPENING_NORMAL_REMOTE,
    MANUAL_OPENING_STARTED_REMOTE,
    MANUAL_OPENING_FINISHED_REMOTE,
    OPENING_NORMAL_PROLONGED_REMOTE,
    TIMED_OPENING_STARTED,
    TIMED_OPENING_FINISHED,
    TIMED_OPENING_SKIPPED,
    TIME_CHANGE_EXECUTED,
    INIT_DATA,
    UPDATE_KEY,
    PERMIT_INIT_TIME,
    INIT_TIME,
    SET_DOOR_ID,
    INIT_DOORAREAS,
    INIT_SPECIALDAY_RESET,
    INIT_SPECIALDAY,
    INIT_TIMED_OPENING,
    INIT_DST,
    INIT_PERMANENT_OPENING_ALLOWED,
    INIT_BLACKLIST,
    INIT_OPENING_TIME,
    KILL_MEDIUM,
    BLACKLIST_VERSION_ERROR,
    FW_UPDATE_PERFORMED,
    FW_UPDATED,
    FW_UPDATED_PARTIAL_FAIL,
    FW_UPDATED_TOTAL_FAIL,
    MIDNIGHT_DEBUG,
    PANIC_EXIT,
    RTC_ERROR,
    RTC_OFFSET,
    ED_DATA_ERROR,
    START_AND_ELOG,
    NONCE_OFFSET,
    BATTERY_EMPTY,
    EEPROM_REINITIALIZED,
    STARTING_UP,
    ELOG_INITIALIZED,
    DEBUG_MESSAGE,
    ABC_INITIALIZED,
    ABC_ADD_DELTABLACKLIST,
    UNEXPECTED_ABC_LOG_RESP,
    IO_INPUT_CHANGED,
    UNKNOWN
}
