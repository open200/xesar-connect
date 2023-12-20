package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.CodingStation
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of coding stations asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of coding stations.
 */
suspend fun XesarConnect.queryCodingStationListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<CodingStation>> {
    return queryListAsync(CodingStation.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a coding station by ID asynchronously.
 *
 * @param id The ID of the coding station to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried coding station.
 */
suspend fun XesarConnect.queryCodingStationByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<CodingStation> {
    return queryElementAsync(CodingStation.QUERY_RESOURCE, id, requestConfig)
}
