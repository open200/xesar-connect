package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.InstallationPointsInZoneChanged
import com.open200.xesar.connect.messages.event.ZoneChanged
import com.open200.xesar.connect.messages.event.ZoneCreated
import com.open200.xesar.connect.messages.event.ZoneDeleted
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.Zone
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of zones asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of zones.
 */
suspend fun XesarConnect.queryZones(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<Zone> {
    return handleQueryListFunction { queryListAsync(Zone.QUERY_RESOURCE, params, requestConfig) }
}

/**
 * Queries a zone by ID asynchronously.
 *
 * @param id The ID of the zone to query.
 * @param requestConfig The request configuration (optional).
 * @return A zone.
 */
suspend fun XesarConnect.queryZoneById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Zone? {
    return handleQueryElementFunction { queryElementAsync(Zone.QUERY_RESOURCE, id, requestConfig) }
}

/**
 * Adds an installation point to a zone asynchronously.
 *
 * @param installationPointId The ID of the installation point.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addInstallationPointToZoneAsync(
    installationPointId: UUID,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<InstallationPointsInZoneChanged> {
    return sendCommandAsync<AddInstallationPointToZoneMapi, InstallationPointsInZoneChanged>(
        Topics.Command.ADD_INSTALLATION_POINT_TO_ZONE,
        Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED,
        true,
        AddInstallationPointToZoneMapi(
            config.uuidGenerator.generateId(),
            installationPointId,
            zoneId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Changes the data of a zone asynchronously.
 *
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeZoneDataAsync(
    name: String,
    description: String,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<ZoneChanged> {
    return sendCommandAsync<ChangeZoneDataMapi, ZoneChanged>(
        Topics.Command.CHANGE_ZONE_DATA,
        Topics.Event.ZONE_CHANGED,
        true,
        ChangeZoneDataMapi(config.uuidGenerator.generateId(), name, description, zoneId, token),
        requestConfig,
    )
}

/**
 * Creates a zone asynchronously.
 *
 * @param installationPoints The list of installation points.
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createZoneAsync(
    installationPoints: List<UUID> = emptyList(),
    name: String,
    description: String? = null,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<ZoneCreated> {
    return sendCommandAsync<CreateZoneMapi, ZoneCreated>(
        Topics.Command.CREATE_ZONE,
        Topics.Event.ZONE_CREATED,
        true,
        CreateZoneMapi(
            config.uuidGenerator.generateId(),
            installationPoints,
            name,
            description,
            zoneId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Deletes a zone asynchronously.
 *
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteZoneAsync(
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<ZoneDeleted> {
    return sendCommandAsync<DeleteZoneMapi, ZoneDeleted>(
        Topics.Command.DELETE_ZONE,
        Topics.Event.ZONE_DELETED,
        true,
        DeleteZoneMapi(config.uuidGenerator.generateId(), zoneId, token),
        requestConfig,
    )
}

/**
 * Removes an installation point from a zone asynchronously.
 *
 * @param installationPointId The ID of the installation point.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.removeInstallationPointFromZoneAsync(
    installationPointId: UUID,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<InstallationPointsInZoneChanged> {
    return sendCommandAsync<RemoveInstallationPointFromZoneMapi, InstallationPointsInZoneChanged>(
        Topics.Command.REMOVE_INSTALLATION_POINT_FROM_ZONE,
        Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED,
        true,
        RemoveInstallationPointFromZoneMapi(
            config.uuidGenerator.generateId(),
            installationPointId,
            zoneId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Changes the value of custom data field of a zone.
 *
 * @param id The ID of the zone.
 * @param metadataId The metadataID of the data field.
 * @param value The new value of the field.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeZoneMetadataValueAsync(
    id: UUID,
    metadataId: UUID,
    value: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<ZoneChanged> {
    return sendCommandAsync<ChangeZoneMetadataValueMapi, ZoneChanged>(
        Topics.Command.CHANGE_ZONE_METADATA_VALUE,
        Topics.Event.ZONE_CHANGED,
        true,
        ChangeZoneMetadataValueMapi(
            config.uuidGenerator.generateId(),
            id,
            metadataId,
            value,
            token,
        ),
        requestConfig,
    )
}

/**
 * Retrieves a cold stream of [Zone] objects, fetching them incrementally in smaller,more manageable
 * chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit] to choose
 * the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [Zone] objects
 */
fun XesarConnect.queryStreamZone(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<Zone> {
    return queryStream(Zone.QUERY_RESOURCE, params, requestConfig)
}
