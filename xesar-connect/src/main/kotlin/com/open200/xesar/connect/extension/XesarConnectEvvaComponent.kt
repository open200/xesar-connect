package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.EvvaComponent
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of evva components asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of evva components.
 */
suspend fun XesarConnect.queryEvvaComponentListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<EvvaComponent>> {
    return queryListAsync(EvvaComponent.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries an evva component by ID asynchronously.
 *
 * @param id The ID of the evva component to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried evva component.
 */
suspend fun XesarConnect.queryEvvaComponentByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<EvvaComponent> {
    return queryElementAsync(EvvaComponent.QUERY_RESOURCE, id, requestConfig)
}

fun XesarConnect.queryStreamEvvaComponent(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<EvvaComponent> {
    return queryStream(EvvaComponent.QUERY_RESOURCE, params, requestConfig)
}
