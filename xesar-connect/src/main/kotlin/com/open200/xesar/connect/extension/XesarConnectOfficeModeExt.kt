package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.OfficeMode
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of office modes asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of office modes.
 */
suspend fun XesarConnect.queryOfficeModeListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<OfficeMode>> {
    return queryListAsync(OfficeMode.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries an office mode by ID asynchronously.
 *
 * @param id The ID of the office mode to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried office mode.
 */
suspend fun XesarConnect.queryOfficeModeByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<OfficeMode> {
    return queryElementAsync(OfficeMode.QUERY_RESOURCE, id, requestConfig)
}

fun XesarConnect.queryStreamOfficeMode(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<OfficeMode> {
    return queryStream(OfficeMode.QUERY_RESOURCE, params, requestConfig)
}
