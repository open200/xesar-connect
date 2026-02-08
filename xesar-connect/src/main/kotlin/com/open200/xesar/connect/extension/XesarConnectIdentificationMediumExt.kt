package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.messages.AssignAuthorizationProfileToMediumResult
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.RequestAddMediumToInstallationResult
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.IdentificationMedium
import com.open200.xesar.connect.messages.query.QueryList
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of identification media asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of identification media.
 */
suspend fun XesarConnect.queryIdentificationMediums(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<IdentificationMedium> {
    return handleQueryListFunction {
        queryListAsync(IdentificationMedium.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries an identification media by ID asynchronously.
 *
 * @param id The ID of the identification media to query.
 * @param requestConfig The request configuration (optional).
 * @return An identification media.
 */
suspend fun XesarConnect.queryIdentificationMediumById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): IdentificationMedium? {
    return handleQueryElementFunction {
        queryElementAsync(IdentificationMedium.QUERY_RESOURCE, id, requestConfig)
    }
}

/**
 * Queries an identification medium by medium identifier.
 *
 * @param mediumIdentifierValue The medium identifier.
 * @param requestConfig The request configuration (optional).
 * @return An identification medium.
 */
suspend fun XesarConnect.queryIdentificationMediumByMediumIdentifier(
    mediumIdentifierValue: Int,
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): IdentificationMedium? {
    val filters =
        (params?.filters ?: emptyList()) +
            Query.Params.Filter(
                field = "mediumIdentifier",
                value = mediumIdentifierValue.toString(),
                type = FilterType.EQ,
            )

    val paramsMedium =
        Query.Params(
            pageLimit = params?.pageLimit,
            pageOffset = params?.pageOffset,
            language = params?.language,
            sort = params?.sort,
            filters = filters,
        )

    val queryListMedia = handleQueryListFunction {
        queryListAsync<IdentificationMedium>(
            IdentificationMedium.QUERY_RESOURCE,
            paramsMedium,
            requestConfig,
        )
    }

    return when {
        queryListMedia.data.isEmpty() -> null
        queryListMedia.data.size > 1 ->
            throw MediumListSizeException(
                "Expected exactly one element in the list with mediumIdentifier $mediumIdentifierValue, but found ${queryListMedia.data.size} elements"
            )
        else -> queryListMedia.data.first()
    }
}

/**
 * Adds an individual authorization for an installation point to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to add the authorization to.
 * @param authorization The authorization to add.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addInstallationPointAuthorizationToMediumAsync(
    mediumId: UUID,
    authorization: AuthorizationData,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<IndividualAuthorizationsAddedToMedium> {
    return sendCommandAsync<
        AddInstallationPointAuthorizationToMediumMapi,
        IndividualAuthorizationsAddedToMedium,
    >(
        Topics.Command.ADD_INSTALLATION_POINT_AUTHORIZATION_TO_MEDIUM,
        Topics.Event.INDIVIDUAL_AUTHORIZATIONS_ADDED_TO_MEDIUM,
        true,
        AddInstallationPointAuthorizationToMediumMapi(
            config.uuidGenerator.generateId(),
            mediumId,
            authorization,
            token,
        ),
        requestConfig,
    )
}

/**
 * Adds an individual authorization for a zone to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to add the authorization to.
 * @param authorizationData The authorization to add.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addZoneAuthorizationToMediumAsync(
    mediumId: UUID,
    authorizationData: AuthorizationData,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<IndividualAuthorizationsAddedToMedium> {
    return sendCommandAsync<
        AddZoneAuthorizationToMediumMapi,
        IndividualAuthorizationsAddedToMedium,
    >(
        Topics.Command.ADD_ZONE_AUTHORIZATION_TO_MEDIUM,
        Topics.Event.INDIVIDUAL_AUTHORIZATIONS_ADDED_TO_MEDIUM,
        true,
        AddZoneAuthorizationToMediumMapi(
            config.uuidGenerator.generateId(),
            mediumId,
            authorizationData,
            token,
        ),
        requestConfig,
    )
}

/**
 * Assigns a person to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to assign the person to.
 * @param personId The ID of the person to assign.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.assignPersonToMediumAsync(
    mediumId: UUID,
    personId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumPersonChanged> {
    return sendCommandAsync<AssignPersonToMediumMapi, MediumPersonChanged>(
        Topics.Command.ASSIGN_PERSON_TO_MEDIUM,
        Topics.Event.MEDIUM_PERSON_CHANGED,
        true,
        AssignPersonToMediumMapi(config.uuidGenerator.generateId(), mediumId, personId, token),
        requestConfig,
    )
}

/**
 * Locks a medium asynchronously.
 *
 * @param mediumId The ID of the medium to lock.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.lockMediumAsync(
    mediumId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumLocked> {
    return sendCommandAsync<LockMediumMapi, MediumLocked>(
        Topics.Command.LOCK_MEDIUM,
        Topics.Event.MEDIUM_LOCKED,
        true,
        LockMediumMapi(config.uuidGenerator.generateId(), mediumId, token),
        requestConfig,
    )
}

/**
 * Removes an individual authorization for an installation point from a medium asynchronously.
 *
 * @param mediumId The ID of the medium to remove the authorization from.
 * @param installationPointAuthorization The installation point id.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.removeInstallationPointAuthorizationFromMediumAsync(
    mediumId: UUID,
    installationPointAuthorization: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<IndividualAuthorizationsDeleted> {
    return sendCommandAsync<
        RemoveInstallationPointAuthorizationFromMediumMapi,
        IndividualAuthorizationsDeleted,
    >(
        Topics.Command.REMOVE_INSTALLATION_POINT_AUTHORIZATION_FROM_MEDIUM,
        Topics.Event.INDIVIDUAL_AUTHORIZATIONS_DELETED,
        true,
        RemoveInstallationPointAuthorizationFromMediumMapi(
            config.uuidGenerator.generateId(),
            installationPointAuthorization,
            mediumId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Removes an individual authorization for a zone from a medium asynchronously.
 *
 * @param mediumId The ID of the medium to remove the authorization from.
 * @param zoneAuthorization The zone id.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.removeZoneAuthorizationFromMediumAsync(
    mediumId: UUID,
    zoneAuthorization: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<IndividualAuthorizationsDeleted> {
    return sendCommandAsync<RemoveZoneAuthorizationFromMediumMapi, IndividualAuthorizationsDeleted>(
        Topics.Command.REMOVE_ZONE_AUTHORIZATION_FROM_MEDIUM,
        Topics.Event.INDIVIDUAL_AUTHORIZATIONS_DELETED,
        true,
        RemoveZoneAuthorizationFromMediumMapi(
            config.uuidGenerator.generateId(),
            zoneAuthorization,
            mediumId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the access begin of an identification medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the access begin.
 * @param accessBeginAt The access begin to set (optional). If the parameter is null, the access
 *   begin is automatically set to the current LocalDateTime minus 2 hours by Xesar.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setAccessBeginAtAsync(
    mediumId: UUID,
    accessBeginAt: LocalDateTime?,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<SetAccessBeginAtMapi, MediumChanged>(
        Topics.Command.SET_ACCESS_BEGIN_AT,
        Topics.Event.MEDIUM_CHANGED,
        true,
        SetAccessBeginAtMapi(config.uuidGenerator.generateId(), accessBeginAt, mediumId, token),
        requestConfig,
    )
}

/**
 * Sets the access end of an identification medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the access end.
 * @param accessEndAt The access end to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setAccessEndAtAsync(
    mediumId: UUID,
    accessEndAt: LocalDateTime? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<SetAccessEndAtMapi, MediumChanged>(
        Topics.Command.SET_ACCESS_END_AT,
        Topics.Event.MEDIUM_CHANGED,
        true,
        SetAccessEndAtMapi(config.uuidGenerator.generateId(), accessEndAt, mediumId, token),
        requestConfig,
    )
}

/**
 * Sets the disengage period on a medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the disengage period.
 * @param disengagePeriod The disengage period to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDisengagePeriodOnMediumAsync(
    mediumId: UUID,
    disengagePeriod: DisengagePeriod,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<SetDisengagePeriodOnMediumMapi, MediumChanged>(
        Topics.Command.SET_DISENGAGE_PERIOD_ON_MEDIUM,
        Topics.Event.MEDIUM_CHANGED,
        true,
        SetDisengagePeriodOnMediumMapi(
            config.uuidGenerator.generateId(),
            disengagePeriod,
            mediumId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the label on a medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the label.
 * @param label The label to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setLabelOnMediumAsync(
    mediumId: UUID,
    label: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<SetLabelOnMediumMapi, MediumChanged>(
        Topics.Command.SET_LABEL_ON_MEDIUM,
        Topics.Event.MEDIUM_CHANGED,
        true,
        SetLabelOnMediumMapi(config.uuidGenerator.generateId(), mediumId, label, token),
        requestConfig,
    )
}

/**
 * Sets the validity duration on a medium asynchronously.
 *
 * @param mediumId The ID of the medium to set the validity duration.
 * @param validityDuration The validity duration to set.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setValidityDurationAsync(
    mediumId: UUID,
    validityDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<SetValidityDurationMapi, MediumChanged>(
        Topics.Command.SET_VALIDITY_DURATION,
        Topics.Event.MEDIUM_CHANGED,
        true,
        SetValidityDurationMapi(
            config.uuidGenerator.generateId(),
            validityDuration,
            mediumId,
            token,
        ),
        requestConfig,
    )
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
suspend fun XesarConnect.withdrawAuthorizationProfileFromMediumAsync(
    mediumId: UUID,
    authorizationProfileId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<AuthorizationProfileWithdrawnFromMedium> {
    return sendCommandAsync<
        WithdrawAuthorizationProfileFromMediumMapi,
        AuthorizationProfileWithdrawnFromMedium,
    >(
        Topics.Command.WITHDRAW_AUTHORIZATION_PROFILE_FROM_MEDIUM,
        Topics.Event.AUTHORIZATION_PROFILE_WITHDRAWN_FROM_MEDIUM,
        true,
        WithdrawAuthorizationProfileFromMediumMapi(
            config.uuidGenerator.generateId(),
            authorizationProfileId,
            mediumId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Assigns an authorization profile to a medium asynchronously.
 *
 * @param mediumId The ID of the medium to assign the authorization profile to.
 * @param authorizationProfileId The ID of the authorization profile to assign.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.assignAuthorizationProfileToMediumAsync(
    mediumId: UUID,
    authorizationProfileId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): AssignAuthorizationProfileToMediumResult {
    val commandId = config.uuidGenerator.generateId()
    val assignAuthorizationProfileToMediumResult =
        sendCommandAsync<
            AssignAuthorizationProfileToMediumMapi,
            MediumChanged,
            MediumAuthorizationProfileChanged,
        >(
            Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM,
            Topics.Event.MEDIUM_CHANGED,
            false,
            Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
            false,
            AssignAuthorizationProfileToMediumMapi(
                commandId,
                authorizationProfileId,
                mediumId,
                token,
            ),
            requestConfig,
        )

    return AssignAuthorizationProfileToMediumResult(
        assignAuthorizationProfileToMediumResult.first,
        assignAuthorizationProfileToMediumResult.second,
        assignAuthorizationProfileToMediumResult.third,
    )
}

/**
 * Requests to add a medium to an installation asynchronously.
 *
 * @param mediumId The ID of the medium to add to the installation.
 * @param hardwareId The hardware ID of the medium to add to the installation.
 * @param terminalId The ID of the terminal to add the medium to.
 * @param label The label of the medium to add to the installation (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the result of the request to add a medium to an
 *   installation.
 */
suspend fun XesarConnect.requestToAddMediumToInstallationAsync(
    mediumId: UUID,
    hardwareId: String,
    terminalId: UUID,
    label: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): RequestAddMediumToInstallationResult {
    val commandId = config.uuidGenerator.generateId()
    val requestAddMediumToInstallationResult =
        sendCommandAsync<
            RequestAddMediumToInstallationMapi,
            AddMediumToInstallationRequested,
            MediumAddedToInstallation,
        >(
            Topics.Command.REQUEST_ADD_MEDIUM_TO_INSTALLATION,
            Topics.Event.ADD_MEDIUM_TO_INSTALLATION_REQUESTED,
            true,
            Topics.Event.MEDIUM_ADDED_TO_INSTALLATION,
            true,
            RequestAddMediumToInstallationMapi(
                hardwareId,
                mediumId,
                terminalId,
                label,
                commandId,
                token,
            ),
            requestConfig,
        )
    return RequestAddMediumToInstallationResult(
        requestAddMediumToInstallationResult.first,
        requestAddMediumToInstallationResult.second,
        requestAddMediumToInstallationResult.third,
    )
}

/**
 * Changes the value of custom data field of a medium asynchronously.
 *
 * @param id The ID of the medium.
 * @param metadataId The metadataID of the data field.
 * @param value The new value of the field.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeMediumMetadataValueAsync(
    id: UUID,
    metadataId: UUID,
    value: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumChanged> {
    return sendCommandAsync<ChangeMediumMetadataValueMapi, MediumChanged>(
        Topics.Command.CHANGE_MEDIUM_METADATA_VALUE,
        Topics.Event.MEDIUM_CHANGED,
        true,
        ChangeMediumMetadataValueMapi(
            config.uuidGenerator.generateId(),
            id,
            metadataId,
            value,
            token,
        ),
        requestConfig,
    )
}

/**
 * Retrieves a cold stream of [IdentificationMedium] objects, fetching them incrementally in
 * smaller,more manageable chunks rather than retrieving the entire dataset at once. Use
 * [Query.Params.pageLimit] to choose the size of one chunk. Use [Query.Params.pageOffset] to choose
 * at which offset to start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [IdentificationMedium] objects
 */
fun XesarConnect.queryStreamIdentificationMedium(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<IdentificationMedium> {
    return queryStream(IdentificationMedium.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Unassigns a person from a medium asynchronously.
 *
 * @param mediumId The ID of the medium to unassign the person from.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.unassignPersonFromMediumAsync(
    mediumId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<MediumPersonChanged> {
    return sendCommandAsync<UnassignPersonFromMediumMapi, MediumPersonChanged>(
        Topics.Command.UNASSIGN_PERSON_FROM_MEDIUM,
        Topics.Event.MEDIUM_PERSON_CHANGED,
        true,
        UnassignPersonFromMediumMapi(config.uuidGenerator.generateId(), mediumId, token),
        requestConfig,
    )
}
