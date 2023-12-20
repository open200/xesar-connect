package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.User
import kotlinx.coroutines.Deferred

/**
 * Queries the list of users asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of users.
 */
suspend fun XesarConnect.queryUserListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<User>> {
    return queryListAsync(User.QUERY_RESOURCE, params, requestConfig)
}
