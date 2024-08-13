package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.SetPhoneNumberOnSmartphoneResult
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import java.time.LocalDateTime
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

/**
 * Adds a smartphone media to an installation.
 *
 * The AddSmartphoneToInstallationMapi command is designed specifically for adding smartphone
 * identification mediums, while the AddMediumToInstallationMapi command is dedicated to adding
 * passive identification mediums. All other commands related to identification mediums can be used
 * for modifying both smartphone identification mediums and other types of identification mediums.
 *
 * @param accessBeginAt The timestamp when access begins for the smartphone media (optional).
 * @param partitionId The identifier of the partition associated with the smartphone media
 *   (optional).
 * @param disengagePeriod The disengage period of the smartphone media (optional).
 * @param authorizationProfileId The identifier of the authorization profile associated with the
 *   smartphone media (optional).
 * @param individualAuthorizationProfileIds The list of individual authorization profile identifiers
 *   associated with the smartphone media (optional).
 * @param messageLanguage The language for correspondence (optional).
 * @param label The label of the smartphone media (optional).
 * @param accessEndAt The timestamp when access ends for the smartphone media (optional).
 * @param phoneNumber The phone number of the smartphone media (optional).
 * @param validityDuration The duration of validity for the smartphone media (optional).
 * @param personId The unique identifier of the person associated with the smartphone media
 *   (optional).
 * @param id The unique identifier of the smartphone media.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addSmartphoneToInstallationAsync(
    accessBeginAt: LocalDateTime? = null,
    partitionId: UUID? = null,
    disengagePeriod: DisengagePeriod? = null,
    authorizationProfileId: UUID? = null,
    individualAuthorizationProfileIds: List<UUID>? = null,
    messageLanguage: String? = null,
    label: String? = null,
    accessEndAt: LocalDateTime? = null,
    phoneNumber: String? = null,
    validityDuration: Int? = null,
    personId: UUID? = null,
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<SmartphoneAddedToInstallation> {
    return sendCommandAsync<AddSmartphoneToInstallationMapi, SmartphoneAddedToInstallation>(
        Topics.Command.ADD_SMARTPHONE_TO_INSTALLATION,
        Topics.Event.SMARTPHONE_ADDED_TO_INSTALLATION,
        true,
        AddSmartphoneToInstallationMapi(
            config.uuidGenerator.generateId(),
            accessBeginAt,
            partitionId,
            disengagePeriod,
            authorizationProfileId,
            individualAuthorizationProfileIds,
            messageLanguage,
            label,
            accessEndAt,
            phoneNumber,
            validityDuration,
            personId,
            id,
            token),
        requestConfig)
}

/**
 * Manually requests a new registration code for a smartphone media.
 *
 * @param id The id of the smartphone media.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.requestNewRegistrationCodeAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<NewRegistrationCodeRequested> {
    return sendCommandAsync<RequestNewRegistrationCodeMapi, NewRegistrationCodeRequested>(
        Topics.Command.REQUEST_NEW_REGISTRATION_CODE,
        Topics.Event.NEW_REGISTRATION_CODE_REQUESTED,
        true,
        RequestNewRegistrationCodeMapi(config.uuidGenerator.generateId(), id, token),
        requestConfig)
}

/**
 * Sets the message language on a smartphone media.
 *
 * @param id The id of the smartphone media.
 * @param messageLanguage The message language to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setMessageLanguageOnSmartphoneAsync(
    id: UUID,
    messageLanguage: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<SetMessageLanguageOnSmartphoneMapi, MediumChanged>(
        Topics.Command.SET_MESSAGE_LANGUAGE_ON_SMARTPHONE,
        Topics.Event.MEDIUM_CHANGED,
        true,
        SetMessageLanguageOnSmartphoneMapi(
            config.uuidGenerator.generateId(), messageLanguage, id, token),
        requestConfig)
}

/**
 * Unregisters a smartphone media.
 *
 * @param smartphoneMediaId The id of the smartphone media.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.unregisterSmartphone(
    smartphoneMediaId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<SmartphoneUnregistered> {
    return sendCommandAsync<UnregisterSmartphoneMapi, SmartphoneUnregistered>(
        Topics.Command.UNREGISTER_SMARTPHONE,
        Topics.Event.SMARTPHONE_UNREGISTERED,
        true,
        UnregisterSmartphoneMapi(config.uuidGenerator.generateId(), smartphoneMediaId, token),
        requestConfig)
}
