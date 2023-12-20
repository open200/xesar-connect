package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.FindComponent
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.command.RemoteDisengage
import com.open200.xesar.connect.messages.command.RemoteDisengagePermanent
import com.open200.xesar.connect.messages.event.FindComponentPerformed
import com.open200.xesar.connect.messages.event.RemoteDisengagePerformed
import com.open200.xesar.connect.messages.event.RemoteDisengagePermanentPerformed
import com.open200.xesar.connect.messages.query.InstallationPoint
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of installation points asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of installation points.
 */
suspend fun XesarConnect.queryInstallationPointListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<InstallationPoint>> {
    return queryListAsync(InstallationPoint.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries an installation point by ID asynchronously.
 *
 * @param id The ID of the installation point to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried installation point.
 */
suspend fun XesarConnect.queryInstallationPointByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPoint> {
    return queryElementAsync(InstallationPoint.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Enable/disable the beeping signal to find the component.
 *
 * @param installationPointId The ID of the installation point
 * @param enable Enable or disable the beeping signal
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.findComponent(
    installationPointId: UUID,
    enable: Boolean? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<FindComponentPerformed> {
    return sendCommand<FindComponent, FindComponentPerformed>(
        Topics.Command.FIND_COMPONENT,
        FindComponent(
            config.uuidGenerator.generateId(), installationPointId, enable, requestConfig.token),
        requestConfig)
}

/**
 * Remotely disengage an online component.
 *
 * @param installationPointId The ID of the installation point
 * @param extended false for SHORT / true for LONG disengage
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.executeRemoteDisengage(
    installationPointId: UUID,
    extended: Boolean? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<RemoteDisengagePerformed> {
    return sendCommand<RemoteDisengage, RemoteDisengagePerformed>(
        Topics.Command.REMOTE_DISENGAGE,
        RemoteDisengage(
            config.uuidGenerator.generateId(), installationPointId, extended, requestConfig.token),
        requestConfig)
}

/**
 * Remotely disengage an online component permanently.
 *
 * @param installationPointId The ID of the installation point
 * @param enable Enable or disable the permanent disengage
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.executeRemoteDisengagePermanent(
    installationPointId: UUID,
    enable: Boolean? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<RemoteDisengagePermanentPerformed> {
    return sendCommand<RemoteDisengagePermanent, RemoteDisengagePermanentPerformed>(
        Topics.Command.REMOTE_DISENGAGE_PERMANENT,
        RemoteDisengagePermanent(
            config.uuidGenerator.generateId(), installationPointId, enable, requestConfig.token),
        requestConfig)
}
