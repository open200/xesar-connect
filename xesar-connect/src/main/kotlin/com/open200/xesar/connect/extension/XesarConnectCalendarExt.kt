package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.ChangeCalendarMapi
import com.open200.xesar.connect.messages.command.CreateCalendarMapi
import com.open200.xesar.connect.messages.command.DeleteCalendarMapi
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.event.CalendarChanged
import com.open200.xesar.connect.messages.event.CalendarCreated
import com.open200.xesar.connect.messages.event.CalendarDeleted
import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.messages.query.QueryList
import java.time.LocalDate
import java.util.*
import kotlinx.coroutines.flow.Flow

/**
 * Queries the list of calendars asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A response object containing a list of calendars.
 */
suspend fun XesarConnect.queryCalendars(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): QueryList.Response<Calendar> {
    return handleQueryListFunction {
        queryListAsync(Calendar.QUERY_RESOURCE, params, requestConfig)
    }
}

/**
 * Queries a calendar by ID asynchronously.
 *
 * @param id The ID of the calendar to query.
 * @param requestConfig The request configuration (optional).
 * @return A calendar.
 */
suspend fun XesarConnect.queryCalendarById(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Calendar? {
    return handleQueryElementFunction {
        queryElementAsync(Calendar.QUERY_RESOURCE, id, requestConfig)
    }
}

/**
 * Creates a calendar asynchronously.
 *
 * @param name The name of the calendar.
 * @param specialDays The list of special days.
 * @param calendarId The ID of the calendar.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createCalendarAsync(
    name: String,
    specialDays: List<LocalDate> = emptyList(),
    calendarId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<CalendarCreated> {
    return sendCommandAsync<CreateCalendarMapi, CalendarCreated>(
        Topics.Command.CREATE_CALENDAR,
        Topics.Event.CALENDAR_CREATED,
        true,
        CreateCalendarMapi(config.uuidGenerator.generateId(), name, specialDays, calendarId, token),
        requestConfig,
    )
}

/**
 * Changes a calendar asynchronously.
 *
 * @param calendarId The ID of the calendar to change.
 * @param calendarName The new name of the calendar.
 * @param specialDays The new list of special days.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeCalendarAsync(
    calendarId: UUID,
    calendarName: String,
    specialDays: List<LocalDate> = emptyList(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<CalendarChanged> {
    return sendCommandAsync<ChangeCalendarMapi, CalendarChanged>(
        Topics.Command.CHANGE_CALENDAR,
        Topics.Event.CALENDAR_CHANGED,
        true,
        ChangeCalendarMapi(
            config.uuidGenerator.generateId(),
            calendarName,
            specialDays,
            calendarId,
            token,
        ),
        requestConfig,
    )
}

/**
 * Deletes a calendar asynchronously.
 *
 * @param calendarId The ID of the calendar to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteCalendarAsync(
    calendarId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<CalendarDeleted> {
    return sendCommandAsync<DeleteCalendarMapi, CalendarDeleted>(
        Topics.Command.DELETE_CALENDAR,
        Topics.Event.CALENDAR_DELETED,
        true,
        DeleteCalendarMapi(config.uuidGenerator.generateId(), calendarId, token),
        requestConfig,
    )
}

/**
 * Retrieves a cold stream of [Calendar] objects, fetching them incrementally in smaller,more
 * manageable chunks rather than retrieving the entire dataset at once. Use [Query.Params.pageLimit]
 * to choose the size of one chunk. Use [Query.Params.pageOffset] to choose at which offset to
 * start.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A cold flow of [Calendar] objects
 */
fun XesarConnect.queryStreamCalendar(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): Flow<Calendar> {
    return queryStream(Calendar.QUERY_RESOURCE, params, requestConfig)
}
