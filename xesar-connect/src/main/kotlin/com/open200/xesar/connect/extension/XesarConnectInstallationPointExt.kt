package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.AddEvvaComponentResult
import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.CreateInstallationPointResult
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.InstallationPoint
import com.open200.xesar.connect.messages.query.QueryList
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of installation points asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of installation points.
 */
suspend fun XesarConnect.queryInstallationPoints(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): QueryList.Response<InstallationPoint> {
    return handleQueryListFunction {
        queryListAsync(InstallationPoint.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries an installation point by ID asynchronously.
 *
 * @param id The ID of the installation point to query.
 * @param requestConfig The request configuration (optional).
 * @return An installation point.
 */
suspend fun XesarConnect.queryInstallationPointById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): InstallationPoint? {
    return handleQueryElementFunction {
        queryElementAsync(InstallationPoint.QUERY_RESOURCE, id, requestConfig)
    }
}

/**
 * Enables/disables the beeping signal to find the component asynchronously.
 *
 * @param installationPointId The ID of the installation point
 * @param enable Enable or disable the beeping signal
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.findComponentAsync(
    installationPointId: UUID,
    enable: Boolean? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<FindComponentPerformed> {
    return sendCommandAsync<FindComponent, FindComponentPerformed>(
        Topics.Command.FIND_COMPONENT,
        Topics.Event.FIND_COMPONENT_PERFORMED,
        true,
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
suspend fun XesarConnect.executeRemoteDisengageAsync(
    installationPointId: UUID,
    extended: Boolean? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<RemoteDisengagePerformed> {
    return sendCommandAsync<RemoteDisengage, RemoteDisengagePerformed>(
        Topics.Command.REMOTE_DISENGAGE,
        Topics.Event.REMOTE_DISENGAGE_PERFORMED,
        true,
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
suspend fun XesarConnect.executeRemoteDisengagePermanentAsync(
    installationPointId: UUID,
    enable: Boolean? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<RemoteDisengagePermanentPerformed> {
    return sendCommandAsync<RemoteDisengagePermanent, RemoteDisengagePermanentPerformed>(
        Topics.Command.REMOTE_DISENGAGE_PERMANENT,
        Topics.Event.REMOTE_DISENGAGE_PERMANENT_PERFORMED,
        true,
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
suspend fun XesarConnect.changeInstallationPointAsync(
    installationPointId: UUID,
    installationType: String? = null,
    name: String? = null,
    description: String? = null,
    installationId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<ChangeInstallationPointMapi, InstallationPointChanged>(
        Topics.Command.CHANGE_INSTALLATION_POINT,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
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
suspend fun XesarConnect.configureManualOfficeModeAndShopModeMapiAsync(
    shopMode: Boolean? = null,
    manualOfficeMode: Boolean? = null,
    installationPointId: UUID? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<ConfigureManualOfficeModeAndShopModeMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_MANUAL_OFFICE_MODE_AND_SHOP_MODE,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
        ConfigureManualOfficeModeAndShopModeMapi(
            config.uuidGenerator.generateId(),
            shopMode,
            manualOfficeMode,
            installationPointId,
            token),
        requestConfig)
}

/**
 * Activates/deactivates the XVN functionality for an installation point asynchronously.
 *
 * @param upgradeMedia True to activate / false to deactivate XVN functionality
 * @param installationPointId The ID of the installation point
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.configureMediaUpgradeMapiAsync(
    upgradeMedia: Boolean? = null,
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<ConfigureMediaUpgradeMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_MEDIA_UPGRADE,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
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
suspend fun XesarConnect.configureOfficeModeTimeProfileAsync(
    officeModeTimeProfileId: UUID? = null,
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<ConfigureOfficeModeTimeProfileMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_OFFICE_MODE_TIME_PROFILE,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
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
suspend fun XesarConnect.configureReleaseDurationAsync(
    releaseDurationShort: Int? = null,
    releaseDurationLong: Int? = null,
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<ConfigureReleaseDurationMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_RELEASE_DURATION,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
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
suspend fun XesarConnect.deleteInstallationPointAsync(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointDeleted> {
    return sendCommandAsync<DeleteInstallationPointMapi, InstallationPointDeleted>(
        Topics.Command.DELETE_INSTALLATION_POINT,
        Topics.Event.INSTALLATION_POINT_DELETED,
        true,
        DeleteInstallationPointMapi(config.uuidGenerator.generateId(), installationPointId, token),
        requestConfig)
}
/**
 * Adds an EVVA component asynchronously.
 *
 * @param installationPointId The installation point id
 * @param componentType The type of component to add
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.addEvvaComponentAsync(
    installationPointId: UUID,
    componentType: ComponentType,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): AddEvvaComponentResult {
    val commandId = config.uuidGenerator.generateId()
    val addEvvaComponentResult =
        sendCommandAsync<AddEvvaComponentMapi, InstallationPointChanged, EvvaComponentAdded>(
            Topics.Command.ADD_EVVA_COMPONENT,
            Topics.Event.INSTALLATION_POINT_CHANGED,
            true,
            Topics.Event.EVVA_COMPONENT_ADDED,
            false,
            AddEvvaComponentMapi(commandId, installationPointId, componentType, token),
            requestConfig)
    return AddEvvaComponentResult(
        addEvvaComponentResult.first, addEvvaComponentResult.second, addEvvaComponentResult.third)
}

/**
 * Forces the removal of an EVVA component asynchronously.
 *
 * @param installationPointId The installation point id
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.forceRemoveEvvaComponentAsync(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<EvvaComponentRemoved> {
    return sendCommandAsync<ForceRemoveEvvaComponentMapi, EvvaComponentRemoved>(
        Topics.Command.FORCE_REMOVE_EVVA_COMPONENT,
        Topics.Event.EVVA_COMPONENT_REMOVED,
        true,
        ForceRemoveEvvaComponentMapi(config.uuidGenerator.generateId(), installationPointId, token),
        requestConfig)
}
/**
 * Prepares the removal of an EVVA component asynchronously.
 *
 * @param installationPointId The installation point id
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.prepareRemovalOfEvvaComponentAsync(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<EvvaComponentRemovalPrepared> {
    return sendCommandAsync<PrepareRemovalOfEvvaComponentMapi, EvvaComponentRemovalPrepared>(
        Topics.Command.PREPARE_REMOVAL_OF_EVVA_COMPONENT,
        Topics.Event.EVVA_COMPONENT_REMOVAL_PREPARED,
        true,
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
suspend fun XesarConnect.revertPrepareRemovalOfEvvaComponentAsync(
    installationPointId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<PrepareEvvaComponentRemovalReverted> {
    return sendCommandAsync<
        RevertPrepareRemovalOfEvvaComponentMapi, PrepareEvvaComponentRemovalReverted>(
        Topics.Command.REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT,
        Topics.Event.PREPARE_EVVA_COMPONENT_REMOVAL_REVERTED,
        true,
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
suspend fun XesarConnect.setPersonalReferenceDurationForInstallationPointAsync(
    installationPointId: UUID,
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<
        SetPersonalReferenceDurationForInstallationPointMapi, InstallationPointChanged>(
        Topics.Command.SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
        SetPersonalReferenceDurationForInstallationPointMapi(
            config.uuidGenerator.generateId(),
            personalReferenceDuration,
            installationPointId,
            token),
        requestConfig)
}

/**
 * Creates an installation point asynchronously.
 *
 * @param componentType The type of component installed in the installation point.
 * @param installationPointId The id of the installation point.
 * @param linkedInstallationPoints A map consisting of the installation-point id as the key and the
 *   properties as values. Use this if the installation point has more than one evva component (e.g.
 *   the Evva component type 'WallReader2x').
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createInstallationPointAsync(
    componentType: ComponentType,
    installationPointId: UUID,
    linkedInstallationPoints: Map<UUID, CreateInstallationPointMapi.Properties> = emptyMap(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): CreateInstallationPointResult {
    val commandId = config.uuidGenerator.generateId()
    val createInstallationPointResult =
        sendCommandAsync<CreateInstallationPointMapi, InstallationPointCreated, EvvaComponentAdded>(
            Topics.Command.CREATE_INSTALLATION_POINT,
            Topics.Event.INSTALLATION_POINT_CREATED,
            true,
            Topics.Event.EVVA_COMPONENT_ADDED,
            false,
            CreateInstallationPointMapi(
                componentType, installationPointId, linkedInstallationPoints, commandId, token),
            requestConfig)
    return CreateInstallationPointResult(
        createInstallationPointResult.first,
        createInstallationPointResult.second,
        createInstallationPointResult.third)
}

/**
 * Retrieves a cold stream of [InstallationPoint] objects, fetching them incrementally in
 * smaller,more manageable chunks rather than retrieving the entire dataset at once. Use
 * [Query.Params.pageLimit] to choose the size of one chunk. Use [Query.Params.pageOffset] to choose
 * at which offset to start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [InstallationPoint] objects
 */
fun XesarConnect.queryStreamInstallationPoint(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<InstallationPoint> {
    return queryStream(InstallationPoint.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Configures the Bluetooth state of an installation point asynchronously.
 *
 * @param installationPointId The ID of the installation point
 * @param bluetoothState The Bluetooth state to set
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.configureBluetoothStateAsync(
    installationPointId: UUID,
    bluetoothState: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<InstallationPointChanged> {
    return sendCommandAsync<ConfigureBluetoothStateMapi, InstallationPointChanged>(
        Topics.Command.CONFIGURE_BLUETOOTH_STATE,
        Topics.Event.INSTALLATION_POINT_CHANGED,
        true,
        ConfigureBluetoothStateMapi(
            config.uuidGenerator.generateId(), bluetoothState, installationPointId, token),
        requestConfig)
}
