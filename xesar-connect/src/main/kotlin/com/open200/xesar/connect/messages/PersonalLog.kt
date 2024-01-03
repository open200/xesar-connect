package com.open200.xesar.connect.messages

import kotlinx.serialization.Serializable

/**
 * Represents the personal log.
 *
 * @param logMode The log mode .
 * @param days The number of days e.g. for the personal reference duration. Should be set if logMode
 *   is set to 'saveForDays'.
 */
@Serializable
data class PersonalLog(val logMode: PersonalLogModes? = null, val days: Int? = null) {

    enum class PersonalLogModes {
        dontSave,
        saveUnlimited,
        saveForDays
    }
}
