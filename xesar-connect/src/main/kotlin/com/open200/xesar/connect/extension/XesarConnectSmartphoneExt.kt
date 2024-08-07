package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.SetDefaultSmartphoneValidityDurationMapi
import com.open200.xesar.connect.messages.event.PartitionChanged

/**
 * Sets the default validity duration (in days) for all smartphones in a partition.
 *
 * @param validityDuration The validity duration to set in days (Min: 1, Max: 1095).
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultSmartphoneValidityDurationAsync(
    validityDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetDefaultSmartphoneValidityDurationMapi, PartitionChanged>(
        Topics.Command.SET_DEFAULT_SMARTPHONE_VALIDITIY_DURATION,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetDefaultSmartphoneValidityDurationMapi(
            config.uuidGenerator.generateId(), validityDuration, token),
        requestConfig)
}
