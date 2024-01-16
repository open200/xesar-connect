package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.ExceptionTimepointSerie
import com.open200.xesar.connect.messages.TimePointSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.TimeProfile
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Queries the list of time profiles asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of time profiles.
 */
suspend fun XesarConnect.queryTimeProfileListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<TimeProfile>> {
    return queryListAsync(TimeProfile.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a time profile by ID asynchronously.
 *
 * @param id The ID of the time profile to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried time profile.
 */
suspend fun XesarConnect.queryTimeProfileByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<TimeProfile> {
    return queryElementAsync(TimeProfile.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Changes the authorization time profile asynchronously.
 *
 * @param timeProfileId The ID of the time profile to change.
 * @param timeProfileName The name of the time profile.
 * @param description The description of the time profile.
 * @param timeSeries The time series of the time profile.
 * @param exceptionTimeSeries The exception time series of the time profile.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeAuthorizationTimeProfile(
    timeProfileId: UUID,
    timeProfileName: String,
    description: String? = null,
    timeSeries: List<TimeSerie> = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<AuthorizationTimeProfileChanged> {
    return sendCommand<ChangeAuthorizationTimeProfileMapi, AuthorizationTimeProfileChanged>(
        Topics.Command.CHANGE_AUTHORIZATION_TIME_PROFILE,
        ChangeAuthorizationTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeProfileId,
            timeProfileName,
            description,
            timeSeries,
            exceptionTimeSeries,
            requestConfig.token),
        requestConfig)
}

/**
 * Changes the office mode time profile asynchronously.
 *
 * @param timeProfileId The ID of the time profile to change.
 * @param name The name of the time profile.
 * @param description The description of the time profile.
 * @param timeSeries The time series of the time profile.
 * @param exceptionTimeSeries The exception time series of the time profile.
 * @param exceptionTimePointSeries The exception time point series of the time profile.
 * @param timePointSeries The time point series of the time profile.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeOfficeModeTimeProfile(
    timeProfileId: UUID,
    name: String,
    description: String? = null,
    timeSeries: List<TimeSerie>? = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie>? = emptyList(),
    exceptionTimePointSeries: List<ExceptionTimeSerie>? = emptyList(),
    timePointSeries: List<TimePointSerie>? = emptyList(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<OfficeModeTimeProfileChanged> {
    return sendCommand<ChangeOfficeModeTimeProfileMapi, OfficeModeTimeProfileChanged>(
        Topics.Command.CHANGE_OFFICE_MODE_TIME_PROFILE,
        ChangeOfficeModeTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeProfileId,
            name,
            description,
            timeSeries,
            exceptionTimeSeries,
            exceptionTimePointSeries,
            timePointSeries,
            requestConfig.token),
        requestConfig)
}
/**
 * Creates an office mode time profile asynchronously.
 *
 * @param timeSeries The time series of the office mode time profile.
 * @param exceptionTimeSeries The exception time series of the office mode time profile.
 * @param exceptionTimePointSeries The exception time point series of the office mode time profile.
 * @param name The name of the office mode time profile.
 * @param description The description of the office mode time profile.
 * @param timePointSeries The time point series of the office mode time profile.
 * @param timeProfileId The ID of the office mode time profile.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createOfficeModeTimeProfile(
    timeSeries: List<TimeSerie> = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    exceptionTimePointSeries: List<ExceptionTimepointSerie> = emptyList(),
    name: String,
    description: String? = null,
    timePointSeries: List<TimePointSerie> = emptyList(),
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<OfficeModeTimeProfileCreated> {
    return sendCommand<CreateOfficeModeTimeProfileMapi, OfficeModeTimeProfileCreated>(
        Topics.Command.CREATE_OFFICE_MODE_TIME_PROFILE,
        CreateOfficeModeTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeSeries,
            exceptionTimeSeries,
            exceptionTimePointSeries,
            name,
            description,
            timePointSeries,
            timeProfileId,
            requestConfig.token),
        requestConfig)
}

/**
 * Deletes an office mode time profile asynchronously.
 *
 * @param timeProfileId The ID of the office mode time profile to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteOfficeModeTimeProfile(
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<OfficeModeTimeProfileDeleted> {
    return sendCommand<DeleteOfficeModeTimeProfileMapi, OfficeModeTimeProfileDeleted>(
        Topics.Command.DELETE_OFFICE_MODE_TIME_PROFILE,
        DeleteOfficeModeTimeProfileMapi(
            config.uuidGenerator.generateId(), timeProfileId, requestConfig.token),
        requestConfig)
}

/**
 * Creates an authorization time profile asynchronously.
 *
 * @param timeSeries The time series of the authorization time profile.
 * @param exceptionTimeSeries The exception time series of the authorization time profile.
 * @param name The name of the authorization time profile.
 * @param description The description of the authorization time profile.
 * @param timeProfileId The ID of the authorization time profile.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createAuthorizationTimeProfile(
    timeSeries: List<TimeSerie> = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    name: String,
    description: String? = null,
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<AuthorizationTimeProfileCreated> {
    return sendCommand<CreateAuthorizationTimeProfileMapi, AuthorizationTimeProfileCreated>(
        Topics.Command.CREATE_AUTHORIZATION_TIME_PROFILE,
        CreateAuthorizationTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeSeries,
            exceptionTimeSeries,
            name,
            description,
            timeProfileId,
            requestConfig.token),
        requestConfig)
}
/**
 * Deletes an authorization time profile asynchronously.
 *
 * @param timeProfileId The ID of the authorization time profile to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteAuthorizationTimeProfile(
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<AuthorizationTimeProfileDeleted> {
    return sendCommand<DeleteAuthorizationTimeProfileMapi, AuthorizationTimeProfileDeleted>(
        Topics.Command.DELETE_AUTHORIZATION_TIME_PROFILE,
        DeleteAuthorizationTimeProfileMapi(
            config.uuidGenerator.generateId(), timeProfileId, requestConfig.token),
        requestConfig)
}
