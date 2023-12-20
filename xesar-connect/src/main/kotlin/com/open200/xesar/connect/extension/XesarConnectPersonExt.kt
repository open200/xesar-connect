package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.Person
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of persons asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of persons.
 */
suspend fun XesarConnect.queryPersonListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<Person>> {
    return queryListAsync(Person.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a person by ID asynchronously.
 *
 * @param id The ID of the person to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried person.
 */
suspend fun XesarConnect.queryPersonByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<Person> {
    return queryElementAsync(Person.QUERY_RESOURCE, id, requestConfig)
}
