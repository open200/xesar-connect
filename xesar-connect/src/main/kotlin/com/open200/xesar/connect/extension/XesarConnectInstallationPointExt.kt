package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
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
 * Enables/disables the beeping signal to find the component asynchronously.
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
        FindComponent(config.uuidGenerator.generateId(), installationPointId, enable, token),
        requestConfig)
}

/**
 * Remotely disengages an online component asynchronously.
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
        RemoteDisengage(config.uuidGenerator.generateId(), installationPointId, extended, token),
        requestConfig)
}

/**
 * Remotely disengages an online component permanently asynchronously.
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
            config.uuidGenerator.generateId(), installationPointId, enable, token),
        requestConfig)
}

/**
 * Changes an installation point asynchronously.
 *
 * @param installationPointId The ID of the installation point
 * @param installationType The installation type
 * @param name The name of the installation point
 * @param description The description of the installation point
 * @param installationId The ID of the installation
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeInstallationPoint(
    installationPointId: UUID,
    installationType: String? = null,
    name: String? = null,
    description: String? = null,
    installationId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointChanged> {
    return sendCommand<ChangeInstallationPointMapi, InstallationPointChanged>(
        Topics.Command.CHANGE_INSTALLATION_POINT,
        ChangeInstallationPointMapi(
            config.uuidGenerator.generateId(),
            installationPointId,
            installationType,
            name,
            description,
            installationId,
            token),
        requestConfig)
}

/**
 * Configures the shop mode and manual office mode of an installation point asynchronously.
 *
 * @param shopMode The shop mode
 * @param manualOfficeMode The manual office mode
 * @param installationPointId The ID of the installation point
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.configureManualOfficeModeAndShopModeMapi(
    shopMode: Boolean? = null,
    manualOfficeMode: Boolean? = null,
    installationPointId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointChanged> {
    return sendCommand<ConfigureManualOfficeModeAndShopModeMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_MANUAL_OFFICE_MODE_AND_SHOP_MODE,
        ConfigureManualOfficeModeAndShopModeMapi(
            config.uuidGenerator.generateId(),
            shopMode,
            manualOfficeMode,
            installationPointId,
            token),
        buildRequestConfig())
}

/**
 * Activates/deactivates the XVN functionality for an installation point asynchronously.
 *
 * @param upgradeMedia True to activate / false to deactivate XVN functionality
 * @param installationPointId The ID of the installation point
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.configureMediaUpgradeMapi(
    upgradeMedia: Boolean? = null,
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointChanged> {
    return sendCommand<ConfigureMediaUpgradeMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_MEDIA_UPGRADE,
        ConfigureMediaUpgradeMapi(
            config.uuidGenerator.generateId(), upgradeMedia, installationPointId, token),
        requestConfig)
}
/**
 * Configures the office mode time profile of an installation point asynchronously.
 *
 * @param officeModeTimeProfileId The ID of the office mode time profile (Null value for removing
 *   assigned office mode time profile)
 * @param installationPointId The ID of the installation point
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.configureOfficeModeTimeProfile(
    officeModeTimeProfileId: UUID? = null,
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointChanged> {
    return sendCommand<ConfigureOfficeModeTimeProfileMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_OFFICE_MODE_TIME_PROFILE,
        ConfigureOfficeModeTimeProfileMapi(
            config.uuidGenerator.generateId(), installationPointId, officeModeTimeProfileId, token),
        requestConfig)
}

/**
 * Configures the release duration of an installation point asynchronously.
 *
 * @param releaseDurationShort The short release duration value (i.e. how long should the door stay
 *   open) measured in seconds.
 * @param releaseDurationLong The long release duration value (i.e. how long should the door stay
 *   open) measured in seconds.
 * @param installationPointId The installation point id.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.configureReleaseDuration(
    releaseDurationShort: Int? = null,
    releaseDurationLong: Int? = null,
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointChanged> {
    return sendCommand<ConfigureReleaseDurationMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_RELEASE_DURATION,
        ConfigureReleaseDurationMapi(
            config.uuidGenerator.generateId(),
            releaseDurationShort,
            releaseDurationLong,
            installationPointId,
            token),
        requestConfig)
}

/**
 * Deletes an installation point asynchronously.
 *
 * @param installationPointId The installation point id
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteInstallationPoint(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointDeleted> {
    return sendCommand<DeleteInstallationPointMapi, InstallationPointDeleted>(
        Topics.Command.DELETE_INSTALLATION_POINT,
        DeleteInstallationPointMapi(config.uuidGenerator.generateId(), installationPointId, token),
        requestConfig)
}

/**
 * Forces the removal of an EVVA component asynchronously.
 *
 * @param installationPointId The installation point id
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.forceRemoveEvvaComponent(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<EvvaComponentRemoved> {
    return sendCommand<ForceRemoveEvvaComponentMapi, EvvaComponentRemoved>(
        Topics.Command.FORCE_REMOVE_EVVA_COMPONENT,
        ForceRemoveEvvaComponentMapi(config.uuidGenerator.generateId(), installationPointId, token),
        requestConfig)
}
/**
 * Prepares the removal of an EVVA component asynchronously.
 *
 * @param installationPointId The installation point id
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.prepareRemovalOfEvvaComponent(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<EvvaComponentRemovalPrepared> {
    return sendCommand<PrepareRemovalOfEvvaComponentMapi, EvvaComponentRemovalPrepared>(
        Topics.Command.PREPARE_REMOVAL_OF_EVVA_COMPONENT,
        PrepareRemovalOfEvvaComponentMapi(
            config.uuidGenerator.generateId(), installationPointId, token),
        requestConfig)
}

/**
 * Reverts the preparation of the removal of an EVVA component asynchronously.
 *
 * @param installationPointId The installation point id
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.revertPrepareRemovalOfEvvaComponent(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<PrepareEvvaComponentRemovalReverted> {
    return sendCommand<
        RevertPrepareRemovalOfEvvaComponentMapi, PrepareEvvaComponentRemovalReverted>(
        Topics.Command.REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT,
        RevertPrepareRemovalOfEvvaComponentMapi(
            config.uuidGenerator.generateId(), installationPointId, token),
        requestConfig)
}
/**
 * Sets the personal reference duration for an installation point asynchronously.
 *
 * @param installationPointId The installation point id
 * @param personalReferenceDuration The personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setPersonalReferenceDurationForInstallationPoint(
    installationPointId: UUID,
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<InstallationPointChanged> {
    return sendCommand<
        SetPersonalReferenceDurationForInstallationPointMapi, InstallationPointChanged>(
        Topics.Command.SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT,
        SetPersonalReferenceDurationForInstallationPointMapi(
            config.uuidGenerator.generateId(),
            personalReferenceDuration,
            installationPointId,
            token),
        requestConfig)
}
