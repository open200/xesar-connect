package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.IdentificationMediumAccessData
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*

/**
 * Queries the list of identification media access data asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of identification media access data.
 */
suspend fun XesarConnect.queryIdentificationMediumAccessData(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<IdentificationMediumAccessData> {
    return handleQueryListFunction {
        queryListAsync(IdentificationMediumAccessData.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries an identification media access data by xsMediumId asynchronously.
 *
 * @param xsMediumId The xsMediumId of the identification media access data to query.
 * @param requestConfig The request configuration (optional).
 * @return An identification media access data.
 */
suspend fun XesarConnect.queryIdentificationMediumAccessDataById(
    xsMediumId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): IdentificationMediumAccessData? {
    return handleQueryElementFunction {
        queryElementAsync(IdentificationMediumAccessData.QUERY_RESOURCE, xsMediumId, requestConfig)
    }
}
