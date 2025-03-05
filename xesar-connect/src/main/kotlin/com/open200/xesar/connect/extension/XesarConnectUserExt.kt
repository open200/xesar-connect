package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.User
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of users asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of users.
 */
suspend fun XesarConnect.queryUsers(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<User> {
    return handleQueryListFunction { queryListAsync(User.QUERY_RESOURCE, params, requestConfig) }
}

/**
 * Retrieves a cold stream of [User] objects, fetching them incrementally in smaller,more manageable
 * chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit] to choose
 * the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [User] objects
 */
fun XesarConnect.queryStreamUser(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<User> {
    return queryStream(User.QUERY_RESOURCE, params, requestConfig)
}
