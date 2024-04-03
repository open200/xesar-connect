package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.ChangeAuthorizationProfileResult
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.AuthorizationProfile
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of authorization profiles asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of authorization
 *   profiles.
 */
suspend fun XesarConnect.queryAuthorizationProfilesListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<AuthorizationProfile>> {
    return queryListAsync(AuthorizationProfile.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries an authorization profile by ID asynchronously.
 *
 * @param id The ID of the authorization profile to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried authorization profile.
 */
suspend fun XesarConnect.queryAuthorizationProfilesByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<AuthorizationProfile> {
    return queryElementAsync(AuthorizationProfile.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Creates an authorization profile asynchronously.
 *
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile.
 * @param authorizationProfileId The ID of the authorization profile.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createAuthorizationProfileAsync(
    name: String,
    description: String? = null,
    authorizationProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<AuthorizationProfileCreated> {
    return sendCommandAsync<CreateAuthorizationProfileMapi, AuthorizationProfileCreated>(
        Topics.Command.CREATE_AUTHORIZATION_PROFILE,
        Topics.Event.AUTHORIZATION_PROFILE_CREATED,
        true,
        CreateAuthorizationProfileMapi(
            config.uuidGenerator.generateId(), name, description, authorizationProfileId, token),
        requestConfig)
}

/**
 * Deletes an authorization profile asynchronously.
 *
 * @param authorizationProfileId The ID of the authorization profile to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteAuthorizationProfileAsync(
    authorizationProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<AuthorizationProfileDeleted> {
    return sendCommandAsync<DeleteAuthorizationProfileMapi, AuthorizationProfileDeleted>(
        Topics.Command.DELETE_AUTHORIZATION_PROFILE,
        Topics.Event.AUTHORIZATION_PROFILE_DELETED,
        true,
        DeleteAuthorizationProfileMapi(
            config.uuidGenerator.generateId(), authorizationProfileId, token),
        requestConfig)
}
/**
 * Changes an authorization profile asynchronously.
 *
 * @param installationPoints The installation points of the authorization profile.
 * @param manualOfficeMode The manual office mode of the authorization profile.
 * @param name The name of the authorization profile.
 * @param description The description of the authorization profile.
 * @param standardTimeProfile The standard time profile of the authorization profile.
 * @param id The ID of the authorization profile.
 * @param zones The zones of the authorization profile.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the result of the change authorization profile
 *   command.
 */
suspend fun XesarConnect.changeAuthorizationProfileAsync(
    installationPoints: List<Authorization>,
    manualOfficeMode: Boolean,
    name: String,
    description: String? = null,
    standardTimeProfile: UUID? = null,
    id: UUID,
    zones: List<Authorization>,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): ChangeAuthorizationProfileResult {
    val changeAuthorizationProfileResult =
        sendCommandAsync<
            ChangeAuthorizationProfileMapi,
            AuthorizationProfileInfoChanged,
            AuthorizationProfileAccessChanged,
            AuthorizationProfileChanged>(
            Topics.Command.CHANGE_AUTHORIZATION_PROFILE,
            Topics.Event.AUTHORIZATION_PROFILE_INFO_CHANGED,
            true,
            Topics.Event.AUTHORIZATION_PROFILE_ACCESS_CHANGED,
            false,
            Topics.Event.AUTHORIZATION_PROFILE_CHANGED,
            false,
            ChangeAuthorizationProfileMapi(
                installationPoints,
                manualOfficeMode,
                name,
                description,
                standardTimeProfile,
                id,
                zones,
                config.uuidGenerator.generateId(),
                token),
            requestConfig)
    return ChangeAuthorizationProfileResult(
        changeAuthorizationProfileResult.first.first,
        changeAuthorizationProfileResult.first.second,
        changeAuthorizationProfileResult.first.third,
        changeAuthorizationProfileResult.second)
}

/**
 * Retrieves a cold stream of [AuthorizationProfile] objects, fetching them incrementally in
 * smaller,more manageable chunks rather than retrieving the entire dataset at once. Use
 * [Query.Params.pageLimit] to choose the size of one chunk. Use [Query.Params.pageOffset] to choose
 * at which offset to start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [AuthorizationProfile] objects
 */
fun XesarConnect.queryStreamAuthorizationProfile(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<AuthorizationProfile> {
    return queryStream(AuthorizationProfile.QUERY_RESOURCE, params, requestConfig)
}
