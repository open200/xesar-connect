package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.ExceptionTimepointSerie
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.TimePointSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.messages.command.*
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.TimeProfile
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of time profiles asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of time profiles.
 */
suspend fun XesarConnect.queryTimeProfiles(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): QueryList.Response<TimeProfile> {
    return handleQueryListFunction {
        queryListAsync(TimeProfile.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries a time profile by ID asynchronously.
 *
 * @param id The ID of the time profile to query.
 * @param requestConfig The request configuration (optional).
 * @return A time profile.
 */
suspend fun XesarConnect.queryTimeProfileById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): TimeProfile? {
    return handleQueryElementFunction {
        queryElementAsync(TimeProfile.QUERY_RESOURCE, id, requestConfig)
    }
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
suspend fun XesarConnect.changeAuthorizationTimeProfileAsync(
    timeProfileId: UUID,
    timeProfileName: String,
    description: String? = null,
    timeSeries: List<TimeSerie> = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<AuthorizationTimeProfileChanged> {
    return sendCommandAsync<ChangeAuthorizationTimeProfileMapi, AuthorizationTimeProfileChanged>(
        Topics.Command.CHANGE_AUTHORIZATION_TIME_PROFILE,
        Topics.Event.AUTHORIZATION_TIME_PROFILE_CHANGED,
        true,
        ChangeAuthorizationTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeProfileId,
            timeProfileName,
            description,
            timeSeries,
            exceptionTimeSeries,
            token),
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
suspend fun XesarConnect.changeOfficeModeTimeProfileAsync(
    timeProfileId: UUID,
    name: String,
    description: String? = null,
    timeSeries: List<TimeSerie>? = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie>? = emptyList(),
    exceptionTimePointSeries: List<ExceptionTimeSerie>? = emptyList(),
    timePointSeries: List<TimePointSerie>? = emptyList(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<OfficeModeTimeProfileChanged> {
    return sendCommandAsync<ChangeOfficeModeTimeProfileMapi, OfficeModeTimeProfileChanged>(
        Topics.Command.CHANGE_OFFICE_MODE_TIME_PROFILE,
        Topics.Event.OFFICE_MODE_TIME_PROFILE_CHANGED,
        true,
        ChangeOfficeModeTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeProfileId,
            name,
            description,
            timeSeries,
            exceptionTimeSeries,
            exceptionTimePointSeries,
            timePointSeries,
            token),
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
suspend fun XesarConnect.createOfficeModeTimeProfileAsync(
    timeSeries: List<TimeSerie> = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    exceptionTimePointSeries: List<ExceptionTimepointSerie> = emptyList(),
    name: String,
    description: String? = null,
    timePointSeries: List<TimePointSerie> = emptyList(),
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<OfficeModeTimeProfileCreated> {
    return sendCommandAsync<CreateOfficeModeTimeProfileMapi, OfficeModeTimeProfileCreated>(
        Topics.Command.CREATE_OFFICE_MODE_TIME_PROFILE,
        Topics.Event.OFFICE_MODE_TIME_PROFILE_CREATED,
        true,
        CreateOfficeModeTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeSeries,
            exceptionTimeSeries,
            exceptionTimePointSeries,
            name,
            description,
            timePointSeries,
            timeProfileId,
            token),
        requestConfig)
}

/**
 * Deletes an office mode time profile asynchronously.
 *
 * @param timeProfileId The ID of the office mode time profile to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteOfficeModeTimeProfileAsync(
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<OfficeModeTimeProfileDeleted> {
    return sendCommandAsync<DeleteOfficeModeTimeProfileMapi, OfficeModeTimeProfileDeleted>(
        Topics.Command.DELETE_OFFICE_MODE_TIME_PROFILE,
        Topics.Event.OFFICE_MODE_TIME_PROFILE_DELETED,
        true,
        DeleteOfficeModeTimeProfileMapi(config.uuidGenerator.generateId(), timeProfileId, token),
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
suspend fun XesarConnect.createAuthorizationTimeProfileAsync(
    timeSeries: List<TimeSerie> = emptyList(),
    exceptionTimeSeries: List<ExceptionTimeSerie> = emptyList(),
    name: String,
    description: String? = null,
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<AuthorizationTimeProfileCreated> {
    return sendCommandAsync<CreateAuthorizationTimeProfileMapi, AuthorizationTimeProfileCreated>(
        Topics.Command.CREATE_AUTHORIZATION_TIME_PROFILE,
        Topics.Event.AUTHORIZATION_TIME_PROFILE_CREATED,
        true,
        CreateAuthorizationTimeProfileMapi(
            config.uuidGenerator.generateId(),
            timeSeries,
            exceptionTimeSeries,
            name,
            description,
            timeProfileId,
            token),
        requestConfig)
}
/**
 * Deletes an authorization time profile asynchronously.
 *
 * @param timeProfileId The ID of the authorization time profile to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteAuthorizationTimeProfileAsync(
    timeProfileId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): SingleEventResult<AuthorizationTimeProfileDeleted> {
    return sendCommandAsync<DeleteAuthorizationTimeProfileMapi, AuthorizationTimeProfileDeleted>(
        Topics.Command.DELETE_AUTHORIZATION_TIME_PROFILE,
        Topics.Event.AUTHORIZATION_TIME_PROFILE_DELETED,
        true,
        DeleteAuthorizationTimeProfileMapi(config.uuidGenerator.generateId(), timeProfileId, token),
        requestConfig)
}

/**
 * Retrieves a cold stream of [TimeProfile] objects, fetching them incrementally in smaller,more
 * manageable chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit]
 * to choose the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to
 * start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [TimeProfile] objects
 */
fun XesarConnect.queryStreamTimeProfile(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Flow<TimeProfile> {
    return queryStream(TimeProfile.QUERY_RESOURCE, params, requestConfig)
}
