package com.open200.xesar.connect.extension

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.IdentificationMedium
import com.open200.xesar.connect.messages.query.IdentificationMediumAccessData
import com.open200.xesar.connect.messages.query.QueryList
import java.util.UUID

suspend fun XesarConnect.queryIdentificationMediumAccessData(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<IdentificationMediumAccessData> {
    return handleQueryListFunction {
        queryListAsync(IdentificationMediumAccessData.QUERY_RESOURCE, params, requestConfig)
    }
}

// Filters query list by xsMediumId
suspend fun XesarConnect.queryIdentificationMediumAccessDataById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): IdentificationMediumAccessData? {
    return handleQueryElementFunction {
        queryElementAsync(IdentificationMediumAccessData.QUERY_RESOURCE, id, requestConfig)
    }
}


suspend fun XesarConnect.queryIdentificationMediumAccessDataByState(
    state: String,
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<IdentificationMediumAccessData> {
    val filters =
        (params?.filters ?: emptyList()) +
            Query.Params.Filter(
                field = "state",
                value = state,
                type = FilterType.EQ,
            )

    val paramsMedium =
        Query.Params(
            pageLimit = params?.pageLimit,
            pageOffset = params?.pageOffset,
            language = params?.language,
            sort = params?.sort,
            filters = filters,
        )

    return handleQueryListFunction {
        queryListAsync<IdentificationMediumAccessData>(
            IdentificationMediumAccessData.QUERY_RESOURCE,
            paramsMedium,
            requestConfig,
        )
    }
}
