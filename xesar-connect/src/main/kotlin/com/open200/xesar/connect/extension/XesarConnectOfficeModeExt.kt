package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.OfficeMode
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of office modes asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of office modes.
 */
suspend fun XesarConnect.queryOfficeModes(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<OfficeMode> {
    return handleQueryListFunction {
        queryListAsync(OfficeMode.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries an office mode by ID asynchronously.
 *
 * @param id The ID of the office mode to query.
 * @param requestConfig The request configuration (optional).
 * @return An office mode.
 */
suspend fun XesarConnect.queryOfficeModeById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): OfficeMode? {
    return handleQueryElementFunction {
        queryElementAsync(OfficeMode.QUERY_RESOURCE, id, requestConfig)
    }
}

/**
 * Retrieves a cold stream of [OfficeMode] objects, fetching them incrementally in smaller,more
 * manageable chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit]
 * to choose the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to
 * start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [OfficeMode] objects
 */
fun XesarConnect.queryStreamOfficeMode(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<OfficeMode> {
    return queryStream(OfficeMode.QUERY_RESOURCE, params, requestConfig)
}
