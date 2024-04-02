package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.AccessProtocolEvent
import com.open200.xesar.connect.messages.query.QueryList
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of access protocols asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of access protocols.
 */
suspend fun XesarConnect.queryAccessProtocolEventListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<AccessProtocolEvent>> {
    return queryListAsync(AccessProtocolEvent.QUERY_RESOURCE, params, requestConfig)
}

fun XesarConnect.queryStreamAccessProtocolEvent(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<AccessProtocolEvent> {
    return queryStream(AccessProtocolEvent.QUERY_RESOURCE, params, requestConfig)
}
