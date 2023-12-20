package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.AuthorizationProfile
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred

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
