package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of calendars asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of calendars.
 */
suspend fun XesarConnect.queryCalendarListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<Calendar>> {
    return queryListAsync(Calendar.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a calendar by ID asynchronously.
 *
 * @param id The ID of the calendar to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried calendar.
 */
suspend fun XesarConnect.queryCalendarByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<Calendar> {
    return queryElementAsync(Calendar.QUERY_RESOURCE, id, requestConfig)
}
