package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.SetPhoneNumberOnSmartphoneResult
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.SetDefaultSmartphoneValidityDurationMapi
import com.open200.xesar.connect.messages.command.SetPhoneNumberOnSmartphoneMapi
import com.open200.xesar.connect.messages.event.MediumChanged
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.PhoneNumberChanged
import java.util.*

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

/**
 * Sets the phone number on a smartphone access media.
 *
 * @param phoneNumber The phone number to set. Phone numbers starting with a '+' and max. 50
 *   characters are allowed.
 * @param id The id of the smartphone (medium id).
 *
 * If the same phone number is set a second time the "required" [MediumChanged] event will not be
 * triggered from xesar.
 */
suspend fun XesarConnect.setPhoneNumberOnSmartphoneAsync(
    phoneNumber: String,
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SetPhoneNumberOnSmartphoneResult {
    val commandId = config.uuidGenerator.generateId()
    val setPhoneNumberOnSmartPhoneResult =
        sendCommandAsync<SetPhoneNumberOnSmartphoneMapi, MediumChanged, PhoneNumberChanged>(
            Topics.Command.SET_PHONE_NUMBER_ON_SMARTPHONE,
            Topics.Event.MEDIUM_CHANGED,
            true,
            Topics.Event.PHONE_NUMBER_CHANGED,
            false,
            SetPhoneNumberOnSmartphoneMapi(commandId, id, phoneNumber, token),
            requestConfig)
    return SetPhoneNumberOnSmartphoneResult(
        setPhoneNumberOnSmartPhoneResult.first,
        setPhoneNumberOnSmartPhoneResult.second,
        setPhoneNumberOnSmartPhoneResult.third)
}
