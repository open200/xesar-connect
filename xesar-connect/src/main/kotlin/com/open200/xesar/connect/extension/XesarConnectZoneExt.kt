package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.InstallationPointsInZoneChanged
import com.open200.xesar.connect.messages.event.ZoneChanged
import com.open200.xesar.connect.messages.event.ZoneCreated
import com.open200.xesar.connect.messages.event.ZoneDeleted
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.Zone
import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable

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
/**
 * Adds an installation point to a zone asynchronously.
 *
 * @param installationPointId The ID of the installation point.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addInstallationPointToZone(
    installationPointId: UUID,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointsInZoneChanged> {
    return sendCommand<AddInstallationPointToZoneMapi, InstallationPointsInZoneChanged>(
        Topics.Command.ADD_INSTALLATION_POINT_TO_ZONE,
        AddInstallationPointToZoneMapi(
            config.uuidGenerator.generateId(), installationPointId, zoneId, requestConfig.token),
        requestConfig)
}
/**
 * Changes the data of a zone asynchronously.
 *
 * @param name The name of the zone.
 * @param description The description of the zone.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeZoneData(
    name: String,
    description: String,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<ZoneChanged> {
    return sendCommand<ChangeZoneDataMapi, ZoneChanged>(
        Topics.Command.CHANGE_ZONE_DATA,
        ChangeZoneDataMapi(
            config.uuidGenerator.generateId(), name, description, zoneId, requestConfig.token),
        requestConfig)
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
suspend fun XesarConnect.createZone(
    installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    name: String,
    description: String? = null,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<ZoneCreated> {
    return sendCommand<CreateZoneMapi, ZoneCreated>(
        Topics.Command.CREATE_ZONE,
        CreateZoneMapi(
            config.uuidGenerator.generateId(),
            installationPoints,
            name,
            description,
            zoneId,
            requestConfig.token),
        requestConfig)
}
/**
 * Deletes a zone asynchronously.
 *
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteZone(
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<ZoneDeleted> {
    return sendCommand<DeleteZoneMapi, ZoneDeleted>(
        Topics.Command.DELETE_ZONE,
        DeleteZoneMapi(config.uuidGenerator.generateId(), zoneId, requestConfig.token),
        requestConfig)
}
/**
 * Removes an installation point from a zone asynchronously.
 *
 * @param installationPointId The ID of the installation point.
 * @param zoneId The ID of the zone.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.removeInstallationPointFromZone(
    installationPointId: UUID,
    zoneId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointsInZoneChanged> {
    return sendCommand<RemoveInstallationPointFromZoneMapi, InstallationPointsInZoneChanged>(
        Topics.Command.REMOVE_INSTALLATION_POINT_FROM_ZONE,
        RemoveInstallationPointFromZoneMapi(
            config.uuidGenerator.generateId(), installationPointId, zoneId, requestConfig.token),
        requestConfig)
}
