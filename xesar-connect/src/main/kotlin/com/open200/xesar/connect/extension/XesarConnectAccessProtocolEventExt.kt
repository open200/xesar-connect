package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.AccessProtocolEvent
import com.open200.xesar.connect.messages.query.QueryList
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of access protocols asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of access protocols.
 */
suspend fun XesarConnect.queryAccessProtocolEvents(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<AccessProtocolEvent> {
    return handleQueryListFunction {
        queryListAsync(AccessProtocolEvent.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Retrieves a cold stream of [AccessProtocolEvent] objects, fetching them incrementally in
 * smaller,more manageable chunks rather than retrieving the entire dataset at once. Use
 * [Query.Params.pageLimit] to choose the size of one chunk. Use [Query.Params.pageOffset] to choose
 * at which offset to start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [AccessProtocolEvent] objects
 */
fun XesarConnect.queryStreamAccessProtocolEvent(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<AccessProtocolEvent> {
    return queryStream(AccessProtocolEvent.QUERY_RESOURCE, params, requestConfig)
}
