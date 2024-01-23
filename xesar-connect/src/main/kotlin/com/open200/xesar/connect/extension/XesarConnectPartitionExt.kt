package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.PartitionChanged
import java.time.LocalTime
import kotlinx.coroutines.Deferred

/**
 * Sets the daily scheduler execution time in the default partition asynchronously.
 *
 * @param dailySchedulerExecutionTime The time of day when the daily scheduler should be executed
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDailySchedulerExecutionTime(
    dailySchedulerExecutionTime: LocalTime,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PartitionChanged> {
    return sendCommand<SetDailySchedulerExecutionTimeMapi, PartitionChanged>(
        Topics.Command.SET_DAILY_SCHEDULER_EXECUTION_TIME,
        SetDailySchedulerExecutionTimeMapi(
            config.uuidGenerator.generateId(), dailySchedulerExecutionTime, token),
        requestConfig)
}
/**
 * Sets the default validity duration in the default partition asynchronously.
 *
 * @param validityDuration The default validity duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultValidityDuration(
    validityDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PartitionChanged> {
    return sendCommand<SetDefaultValidityDurationMapi, PartitionChanged>(
        Topics.Command.SET_DEFAULT_VALIDITY_DURATION,
        SetDefaultValidityDurationMapi(config.uuidGenerator.generateId(), validityDuration, token),
        requestConfig)
}

/**
 * Sets the default logging of personal data for installation point in the default partition
 * asynchronously.
 *
 * @param personalReferenceDuration The default personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setInstallationPointPersonalReferenceDuration(
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PartitionChanged> {
    return sendCommand<SetInstallationPointPersonalReferenceDurationMapi, PartitionChanged>(
        Topics.Command.SET_INSTALLATION_POINT_PERSONAL_REFERENCE_DURATION,
        SetInstallationPointPersonalReferenceDurationMapi(
            config.uuidGenerator.generateId(), personalReferenceDuration, token),
        requestConfig)
}

/**
 * Sets the default logging of personal data for person in the default partition asynchronously.
 *
 * @param personalReferenceDuration The default personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setPersonPersonalReferenceDuration(
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PartitionChanged> {
    return sendCommand<SetPersonPersonalReferenceDurationMapi, PartitionChanged>(
        Topics.Command.SET_PERSON_PERSONAL_REFERENCE_DURATION,
        SetPersonPersonalReferenceDurationMapi(
            config.uuidGenerator.generateId(), personalReferenceDuration, token),
        requestConfig)
}

/**
 * Sets the replacement medium duration in the default partition asynchronously.
 *
 * @param replacementMediumDuration The replacement medium duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setReplacementMediumDuration(
    replacementMediumDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PartitionChanged> {
    return sendCommand<SetReplacementMediumDurationMapi, PartitionChanged>(
        Topics.Command.SET_REPLACEMENT_MEDIUM_DURATION,
        SetReplacementMediumDurationMapi(
            config.uuidGenerator.generateId(), replacementMediumDuration, token),
        requestConfig)
}
/**
 * Sets the validity duration in the default partition asynchronously.
 *
 * @param validityThreshold The validity threshold
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setValidityThreshold(
    validityThreshold: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PartitionChanged> {
    return sendCommand<SetValidityThresholdMapi, PartitionChanged>(
        Topics.Command.SET_VALIDITY_THRESHOLD,
        SetValidityThresholdMapi(config.uuidGenerator.generateId(), validityThreshold, token),
        requestConfig)
}
