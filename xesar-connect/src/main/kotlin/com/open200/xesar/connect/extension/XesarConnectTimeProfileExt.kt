package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.TimeProfile
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of time profiles asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of time profiles.
 */
suspend fun XesarConnect.queryTimeProfileListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<TimeProfile>> {
    return queryListAsync(TimeProfile.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a time profile by ID asynchronously.
 *
 * @param id The ID of the time profile to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried time profile.
 */
suspend fun XesarConnect.queryTimeProfileByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<TimeProfile> {
    return queryElementAsync(TimeProfile.QUERY_RESOURCE, id, requestConfig)
}
