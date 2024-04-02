package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.ChangeCodingStationMapi
import com.open200.xesar.connect.messages.command.CreateCodingStationMapi
import com.open200.xesar.connect.messages.command.DeleteCodingStationMapi
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.event.CodingStationChanged
import com.open200.xesar.connect.messages.event.CodingStationCreated
import com.open200.xesar.connect.messages.event.CodingStationDeleted
import com.open200.xesar.connect.messages.query.CodingStation
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.Flow

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

/**
 * Creates a coding station asynchronously.
 *
 * @param name The name of the coding station.
 * @param description The description of the coding station (optional).
 * @param codingStationId The ID of the coding station.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createCodingStationAsync(
    name: String,
    description: String? = null,
    codingStationId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<CodingStationCreated> {
    return sendCommandAsync<CreateCodingStationMapi, CodingStationCreated>(
        Topics.Command.CREATE_CODING_STATION,
        Topics.Event.CODING_STATION_CREATED,
        true,
        CreateCodingStationMapi(
            config.uuidGenerator.generateId(), name, description, codingStationId, token),
        requestConfig)
}

/**
 * Changes a coding station asynchronously.
 *
 * @param codingStationId The ID of the coding station.
 * @param name The name of the coding station (optional).
 * @param description The description of the coding station (optional).
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeCodingStationAsync(
    codingStationId: UUID,
    name: String? = null,
    description: String? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<CodingStationChanged> {
    return sendCommandAsync<ChangeCodingStationMapi, CodingStationChanged>(
        Topics.Command.CHANGE_CODING_STATION,
        Topics.Event.CODING_STATION_CHANGED,
        true,
        ChangeCodingStationMapi(
            config.uuidGenerator.generateId(), codingStationId, name, description, token),
        requestConfig)
}

/**
 * Deletes a coding station asynchronously.
 *
 * @param codingStationId The ID of the coding station.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteCodingStationAsync(
    codingStationId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<CodingStationDeleted> {
    return sendCommandAsync<DeleteCodingStationMapi, CodingStationDeleted>(
        Topics.Command.DELETE_CODING_STATION,
        Topics.Event.CODING_STATION_DELETED,
        true,
        DeleteCodingStationMapi(config.uuidGenerator.generateId(), codingStationId, token),
        requestConfig)
}

fun XesarConnect.queryStreamCodingStation(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<CodingStation> {
    return queryStream(CodingStation.QUERY_RESOURCE, params, requestConfig)
}
