package com.open200.xesar.connect.messages.query

import kotlinx.serialization.Serializable

/** Represents the type of event. */
@Serializable
enum class EventType(val eventNumber: Int, val groupOfEvent: GroupOfEvent) {
    NORMAL_OPENING(0x0001, GroupOfEvent.MediumEvents),
    EMERGENCY_OPENING(0x0002, GroupOfEvent.MediumEvents),
    MANUAL_OPENING_STARTED(0x0003, GroupOfEvent.MediumEvents),
    MANUAL_OPENING_FINISHED(0x0004, GroupOfEvent.MediumEvents),
    MEDIA_RESTRICTED(0x0005, GroupOfEvent.MediumEvents),
    PROLONGED_NORMAL_OPENING(0x0007, GroupOfEvent.MediumEvents),
    PROLONGED_MASTER_KEY_OPENING(0x0008, GroupOfEvent.MediumEvents),
    OPENING_MASTER_KEY_IGNORED_BLACKLIST(0x0009, GroupOfEvent.MediumEvents),
    OPENING_MASTER_KEY_PROLONGED_IGNORED_BLACKLIST(0x000a, GroupOfEvent.MediumEvents),
    OPENING_NORMAL_BLACKLISTED(0xA001, GroupOfEvent.MediumEvents),
    OPENING_EMERGENCY_BLACKLISTED(0xA002, GroupOfEvent.MediumEvents),
    MANUAL_OPENING_STARTED_BLACKLISTED(0xA003, GroupOfEvent.MediumEvents),
    OPENING_NORMAL_PROLONGED_BLACKLISTED(0xA007, GroupOfEvent.MediumEvents),
    OPENING_EMERGENCY_PROLONGED_BLACKLISTED(0xA008, GroupOfEvent.MediumEvents),
    OPENING_EMERGENCY_INVALID_BLACKLIST_OR_TIME_BLACKLISTED(0xA009, GroupOfEvent.MediumEvents),
    OPENING_EMERGENCY_PROLONGED_INVALID_BLACKLIST_OR_TIME_BLACKLISTED(
        0xA00A,
        GroupOfEvent.MediumEvents,
    ),
    OPENING_NORMAL_SWITCH(0xC001, GroupOfEvent.AdministrationComponent),
    OPENING_NORMAL_REMOTE(0xB001, GroupOfEvent.AdministrationComponent),
    MANUAL_OPENING_STARTED_REMOTE(0xB003, GroupOfEvent.AdministrationComponent),
    MANUAL_OPENING_FINISHED_REMOTE(0xB004, GroupOfEvent.AdministrationComponent),
    OPENING_NORMAL_PROLONGED_REMOTE(0xB007, GroupOfEvent.AdministrationComponent),
    TIMED_OPENING_STARTED(0x0101, GroupOfEvent.EvvaComponent),
    TIMED_OPENING_FINISHED(0x0102, GroupOfEvent.EvvaComponent),
    TIMED_OPENING_SKIPPED(0x0103, GroupOfEvent.EvvaComponent),
    TIME_CHANGE_EXECUTED(0x0201, GroupOfEvent.EvvaComponent),
    INIT_DATA(0x0301, GroupOfEvent.MaintenanceComponent),
    UPDATE_KEY(0x0302, GroupOfEvent.MaintenanceComponent),
    PERMIT_INIT_TIME(0x0303, GroupOfEvent.MaintenanceComponent),
    INIT_TIME(0x0304, GroupOfEvent.MaintenanceComponent),
    SET_DOOR_ID(0x0305, GroupOfEvent.MaintenanceComponent),
    INIT_DOORAREAS(0x0306, GroupOfEvent.MaintenanceComponent),
    INIT_SPECIALDAY_RESET(0x0307, GroupOfEvent.MaintenanceComponent),
    INIT_SPECIALDAY(0x0308, GroupOfEvent.MaintenanceComponent),
    INIT_TIMED_OPENING(0x0309, GroupOfEvent.MaintenanceComponent),
    INIT_DST(0x030A, GroupOfEvent.MaintenanceComponent),
    INIT_PERMANENT_OPENING_ALLOWED(0x030B, GroupOfEvent.MaintenanceComponent),
    INIT_BLACKLIST(0x030C, GroupOfEvent.MaintenanceComponent),
    INIT_OPENING_TIME(0x030E, GroupOfEvent.MaintenanceComponent),
    KILL_MEDIUM(0x0401, GroupOfEvent.MediumEvents),
    BLACKLIST_VERSION_ERROR(0x0A03, GroupOfEvent.MaintenanceComponent),
    FW_UPDATE_PERFORMED(0x0501, GroupOfEvent.MaintenanceComponent),
    FW_UPDATED(0xA501, GroupOfEvent.MaintenanceComponent),
    FW_UPDATED_PARTIAL_FAIL(0xA502, GroupOfEvent.MaintenanceComponent),
    FW_UPDATED_TOTAL_FAIL(0xA503, GroupOfEvent.MaintenanceComponent),
    MIDNIGHT_DEBUG(0x0C07, GroupOfEvent.MaintenanceComponent),
    PANIC_EXIT(0x0A01, GroupOfEvent.MaintenanceComponent),
    RTC_ERROR(0x0A02, GroupOfEvent.MaintenanceComponent),
    RTC_OFFSET(0xAB03, GroupOfEvent.MaintenanceComponent),
    ED_DATA_ERROR(0x0C06, GroupOfEvent.MaintenanceComponent),
    START_AND_ELOG(0x0C03, GroupOfEvent.MaintenanceComponent),
    NONCE_OFFSET(0xAB04, GroupOfEvent.MaintenanceComponent),
    BATTERY_EMPTY(0x0B01, GroupOfEvent.MaintenanceComponent),
    EEPROM_REINITIALIZED(0x0B02, GroupOfEvent.MaintenanceComponent),
    STARTING_UP(0x0C01, GroupOfEvent.MaintenanceComponent),
    ELOG_INITIALIZED(0x0C02, GroupOfEvent.MaintenanceComponent),
    DEBUG_MESSAGE(0x0D01, GroupOfEvent.NoGroup),
    ABC_INITIALIZED(0x030D, GroupOfEvent.MaintenanceComponent),
    ABC_ADD_DELTABLACKLIST(0x030F, GroupOfEvent.MaintenanceComponent),
    UNEXPECTED_ABC_LOG_RESP(0x0C05, GroupOfEvent.MaintenanceComponent),
    IO_INPUT_CHANGED(0x0601, GroupOfEvent.MaintenanceComponent),
    UNKNOWN(0xFFFF, GroupOfEvent.NoGroup),
}
