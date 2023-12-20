package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.IdentificationMedium
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred

/**
 * Queries the list of identification media asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of identification media.
 */
suspend fun XesarConnect.queryIdentificationMediumListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<IdentificationMedium>> {
    return queryListAsync(IdentificationMedium.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries an identification media by ID asynchronously.
 *
 * @param id The ID of the identification media to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried identification media.
 */
suspend fun XesarConnect.queryIdentificationMediumByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IdentificationMedium> {
    return queryElementAsync(IdentificationMedium.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Queries an access protocol event by ID asynchronously.
 *
 * @param id The ID of the access protocol event to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried access protocol event.
 */
suspend fun XesarConnect.queryIdentificationMediumByMediumIdentifierAsync(
    mediumIdentifierValue: Int,
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<IdentificationMedium?> {
    val filters =
        (params?.filters
            ?: emptyList()) +
            Query.Params.Filter(
                field = "mediumIdentifier",
                value = mediumIdentifierValue.toString(),
                type = FilterType.EQ)

    val paramsMedium =
        Query.Params(
            pageLimit = params?.pageLimit,
            pageOffset = params?.pageOffset,
            language = params?.language,
            sort = params?.sort,
            filters = filters)

    val queryListMedia =
        queryListAsync<IdentificationMedium>(
                IdentificationMedium.QUERY_RESOURCE, paramsMedium, requestConfig)
            .await()

    return when {
        queryListMedia.data.isEmpty() ->
            CompletableDeferred<IdentificationMedium?>().apply { complete(null) }
        queryListMedia.data.size > 1 ->
            CompletableDeferred<IdentificationMedium?>().apply {
                completeExceptionally(
                    MediumListSizeException(
                        "Expected exactly one element in the list with mediumIdentifier $mediumIdentifierValue, but found ${queryListMedia.data.size} elements"))
            }
        else ->
            CompletableDeferred<IdentificationMedium?>().apply {
                complete(queryListMedia.data.first())
            }
    }
}
