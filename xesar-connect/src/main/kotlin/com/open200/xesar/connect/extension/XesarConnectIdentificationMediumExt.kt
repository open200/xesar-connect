package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.IdentificationMedium
import com.open200.xesar.connect.messages.query.QueryList
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Queries the list of identification media asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of identification media.
 */
suspend fun XesarConnect.queryIdentificationMediumListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<IdentificationMedium>> {
    return queryListAsync(IdentificationMedium.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries an identification media by ID asynchronously.
 *
 * @param id The ID of the identification media to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried identification media.
 */
suspend fun XesarConnect.queryIdentificationMediumByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IdentificationMedium> {
    return queryElementAsync(IdentificationMedium.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Queries an access protocol event by ID asynchronously.
 *
 * @param mediumIdentifierValue The medium identifier.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried access protocol event.
 */
suspend fun XesarConnect.queryIdentificationMediumByMediumIdentifierAsync(
    mediumIdentifierValue: Int,
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IdentificationMedium?> {
    val filters =
        (params?.filters
            ?: emptyList()) +
            Query.Params.Filter(
                field = "mediumIdentifier",
                value = mediumIdentifierValue.toString(),
                type = FilterType.EQ)

    val paramsMedium =
        Query.Params(
            pageLimit = params?.pageLimit,
            pageOffset = params?.pageOffset,
            language = params?.language,
            sort = params?.sort,
            filters = filters)

    val queryListMedia =
        queryListAsync<IdentificationMedium>(
                IdentificationMedium.QUERY_RESOURCE, paramsMedium, requestConfig)
            .await()

    return when {
        queryListMedia.data.isEmpty() ->
            CompletableDeferred<IdentificationMedium?>().apply { complete(null) }
        queryListMedia.data.size > 1 ->
            CompletableDeferred<IdentificationMedium?>().apply {
                completeExceptionally(
                    MediumListSizeException(
                        "Expected exactly one element in the list with mediumIdentifier $mediumIdentifierValue, but found ${queryListMedia.data.size} elements"))
            }
        else ->
            CompletableDeferred<IdentificationMedium?>().apply {
                complete(queryListMedia.data.first())
            }
    }
}

/**
 * Adds an individual authorization for an installation point to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to add the authorization to.
 * @param authorization The authorization to add.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addInstallationPointAuthorizationToMedium(
    mediumId: UUID,
    authorization: AuthorizationData,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IndividualAuthorizationsAddedToMedium> {
    return sendCommand<
        AddInstallationPointAuthorizationToMediumMapi, IndividualAuthorizationsAddedToMedium>(
        Topics.Command.ADD_INSTALLATION_POINT_AUTHORIZATION_TO_MEDIUM,
        AddInstallationPointAuthorizationToMediumMapi(
            config.uuidGenerator.generateId(), mediumId, authorization, requestConfig.token),
        requestConfig)
}

/**
 * Adds an individual authorization for a zone to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to add the authorization to.
 * @param authorizationData The authorization to add.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addZoneAuthorizationToMedium(
    mediumId: UUID,
    authorizationData: AuthorizationData,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IndividualAuthorizationsAddedToMedium> {
    return sendCommand<AddZoneAuthorizationToMediumMapi, IndividualAuthorizationsAddedToMedium>(
        Topics.Command.ADD_ZONE_AUTHORIZATION_TO_MEDIUM,
        AddZoneAuthorizationToMediumMapi(
            config.uuidGenerator.generateId(), mediumId, authorizationData, requestConfig.token),
        requestConfig)
}
/**
 * Assigns a person to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to assign the person to.
 * @param personId The ID of the person to assign.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.assignPersonToMedium(
    mediumId: UUID,
    personId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumPersonChanged> {
    return sendCommand<AssignPersonToMediumMapi, MediumPersonChanged>(
        Topics.Command.ASSIGN_PERSON_TO_MEDIUM,
        AssignPersonToMediumMapi(
            config.uuidGenerator.generateId(), mediumId, personId, requestConfig.token),
        requestConfig)
}

/**
 * Locks a medium asynchronously.
 *
 * @param mediumId The ID of the medium to lock.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.lockMedium(
    mediumId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumLocked> {
    return sendCommand<LockMediumMapi, MediumLocked>(
        Topics.Command.LOCK_MEDIUM,
        LockMediumMapi(config.uuidGenerator.generateId(), mediumId, requestConfig.token),
        requestConfig)
}

/**
 * Removes an individual authorization for an installation point from a medium asynchronously.
 *
 * @param mediumId The ID of the medium to remove the authorization from.
 * @param installationPointAuthorization The installation point id.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.removeInstallationPointAuthorizationFromMedium(
    mediumId: UUID,
    installationPointAuthorization: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IndividualAuthorizationsDeleted> {
    return sendCommand<
        RemoveInstallationPointAuthorizationFromMediumMapi, IndividualAuthorizationsDeleted>(
        Topics.Command.REMOVE_INSTALLATION_POINT_AUTHORIZATION_FROM_MEDIUM,
        RemoveInstallationPointAuthorizationFromMediumMapi(
            config.uuidGenerator.generateId(),
            installationPointAuthorization,
            mediumId,
            requestConfig.token),
        requestConfig)
}
/**
 * Removes an individual authorization for a zone from a medium asynchronously.
 *
 * @param mediumId The ID of the medium to remove the authorization from.
 * @param zoneAuthorization The zone id.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.removeZoneAuthorizationFromMedium(
    mediumId: UUID,
    zoneAuthorization: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IndividualAuthorizationsDeleted> {
    return sendCommand<RemoveZoneAuthorizationFromMediumMapi, IndividualAuthorizationsDeleted>(
        Topics.Command.REMOVE_ZONE_AUTHORIZATION_FROM_MEDIUM,
        RemoveZoneAuthorizationFromMediumMapi(
            config.uuidGenerator.generateId(), zoneAuthorization, mediumId, requestConfig.token),
        requestConfig)
}

/**
 * Sets the access begin of an identification medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the access begin.
 * @param accessBeginAt The access begin to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setAccessBeginAt(
    mediumId: UUID,
    accessBeginAt: LocalDateTime,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumChanged> {
    return sendCommand<SetAccessBeginAtMapi, MediumChanged>(
        Topics.Command.SET_ACCESS_BEGIN_AT,
        SetAccessBeginAtMapi(
            config.uuidGenerator.generateId(), accessBeginAt, mediumId, requestConfig.token),
        requestConfig)
}
/**
 * Sets the access end of an identification medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the access end.
 * @param accessEndAt The access end to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setAccessEndAt(
    mediumId: UUID,
    accessEndAt: LocalDateTime? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumChanged> {
    return sendCommand<SetAccessEndAtMapi, MediumChanged>(
        Topics.Command.SET_ACCESS_END_AT,
        SetAccessEndAtMapi(
            config.uuidGenerator.generateId(), accessEndAt, mediumId, requestConfig.token),
        requestConfig)
}

/**
 * Sets the disengage period on a medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the disengage period.
 * @param disengagePeriod The disengage period to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDisengagePeriodOnMedium(
    mediumId: UUID,
    disengagePeriod: DisengagePeriod,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumChanged> {
    return sendCommand<SetDisengagePeriodOnMediumMapi, MediumChanged>(
        Topics.Command.SET_DISENGAGE_PERIOD_ON_MEDIUM,
        SetDisengagePeriodOnMediumMapi(
            config.uuidGenerator.generateId(), disengagePeriod, mediumId, requestConfig.token),
        requestConfig)
}
/**
 * Sets the label on a medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the label.
 * @param label The label to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setLabelOnMedium(
    mediumId: UUID,
    label: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumChanged> {
    return sendCommand<SetLabelOnMediumMapi, MediumChanged>(
        Topics.Command.SET_LABEL_ON_MEDIUM,
        SetLabelOnMediumMapi(
            config.uuidGenerator.generateId(), mediumId, label, requestConfig.token),
        requestConfig)
}
/**
 * Sets the validity duration on a medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the validity duration.
 * @param validityDuration The validity duration to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setValidityDuration(
    mediumId: UUID,
    validityDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<MediumChanged> {
    return sendCommand<SetValidityDurationMapi, MediumChanged>(
        Topics.Command.SET_VALIDITY_DURATION,
        SetValidityDurationMapi(
            config.uuidGenerator.generateId(), validityDuration, mediumId, requestConfig.token),
        requestConfig)
}

/**
 * Withdraws an authorization profile from a medium asynchronously. Withdraw means that we don't
 * have the physical medium in our hands. We remove the authorizations. The next time the medium is
 * updated, authorizations will be removed from the physical card. Most likely this will happen at
 * an online-wallreader.
 *
 * @param mediumId The ID of the medium to withdraw the authorization profile from.
 * @param authorizationProfileId The ID of the authorization profile to withdraw.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.withdrawAuthorizationProfileFromMedium(
    mediumId: UUID,
    authorizationProfileId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<AuthorizationProfileWithdrawnFromMedium> {
    return sendCommand<
        WithdrawAuthorizationProfileFromMediumMapi, AuthorizationProfileWithdrawnFromMedium>(
        Topics.Command.WITHDRAW_AUTHORIZATION_PROFILE_FROM_MEDIUM,
        WithdrawAuthorizationProfileFromMediumMapi(
            config.uuidGenerator.generateId(),
            authorizationProfileId,
            mediumId,
            requestConfig.token),
        requestConfig)
}
