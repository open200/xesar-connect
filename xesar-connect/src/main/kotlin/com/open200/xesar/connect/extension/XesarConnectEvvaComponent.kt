package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.EvvaComponent
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of evva components asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of evva components.
 */
suspend fun XesarConnect.queryEvvaComponents(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): QueryList.Response<EvvaComponent> {
    return handleQueryListFunction {
        queryListAsync(EvvaComponent.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries an evva component by ID asynchronously.
 *
 * @param id The ID of the evva component to query.
 * @param requestConfig The request configuration (optional).
 * @return An evva component.
 */
suspend fun XesarConnect.queryEvvaComponentById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): EvvaComponent? {
    return handleQueryElementFunction {
        queryElementAsync(EvvaComponent.QUERY_RESOURCE, id, requestConfig)
    }
}

/**
 * Retrieves a cold stream of [EvvaComponent] objects, fetching them incrementally in smaller,more
 * manageable chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit]
 * to choose the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to
 * start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [EvvaComponent] objects
 */
fun XesarConnect.queryStreamEvvaComponent(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<EvvaComponent> {
    return queryStream(EvvaComponent.QUERY_RESOURCE, params, requestConfig)
}
