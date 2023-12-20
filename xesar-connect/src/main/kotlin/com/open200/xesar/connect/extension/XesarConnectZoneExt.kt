package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.Zone
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of zones asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves a response containing a list of zones.
 */
suspend fun XesarConnect.queryZoneListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<Zone>> {
    return queryListAsync(Zone.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a zone by ID asynchronously.
 *
 * @param id The ID of the zone to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried zone.
 */
suspend fun XesarConnect.queryZoneByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<Zone> {
    return queryElementAsync(Zone.QUERY_RESOURCE, id, requestConfig)
}
