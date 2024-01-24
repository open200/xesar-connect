package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.ChangeCalendarMapi
import com.open200.xesar.connect.messages.command.CreateCalendarMapi
import com.open200.xesar.connect.messages.command.DeleteCalendarMapi
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.event.CalendarChanged
import com.open200.xesar.connect.messages.event.CalendarCreated
import com.open200.xesar.connect.messages.event.CalendarDeleted
import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.utils.LocalDateSerializer
import java.time.LocalDate
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.serialization.Serializable

/**
 * Queries the list of calendars asynchronously.
 *
 * @param params The query parameters (optional).
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing a list of calendars.
 */
suspend fun XesarConnect.queryCalendarListAsync(
    params: Query.Params? = null,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<QueryList.Response<Calendar>> {
    return queryListAsync(Calendar.QUERY_RESOURCE, params, requestConfig)
}

/**
 * Queries a calendar by ID asynchronously.
 *
 * @param id The ID of the calendar to query.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to the queried calendar.
 */
suspend fun XesarConnect.queryCalendarByIdAsync(
    id: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<Calendar> {
    return queryElementAsync(Calendar.QUERY_RESOURCE, id, requestConfig)
}

/**
 * Creates a calendar asynchronously.
 *
 * @param name The name of the calendar.
 * @param specialDays The list of special days.
 * @param calendarId The ID of the calendar.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.createCalendar(
    name: String,
    specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate> = emptyList(),
    calendarId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<CalendarCreated> {
    return sendCommand<CreateCalendarMapi, CalendarCreated>(
        Topics.Command.CREATE_CALENDAR,
        CreateCalendarMapi(config.uuidGenerator.generateId(), name, specialDays, calendarId, token),
        requestConfig)
}

/**
 * Changes a calendar asynchronously.
 *
 * @param calendarId The ID of the calendar to change.
 * @param calendarName The new name of the calendar.
 * @param specialDays The new list of special days.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.changeCalendar(
    calendarId: UUID,
    calendarName: String,
    specialDays: List<LocalDate> = emptyList(),
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<CalendarChanged> {
    return sendCommand<ChangeCalendarMapi, CalendarChanged>(
        Topics.Command.CHANGE_CALENDAR,
        ChangeCalendarMapi(
            config.uuidGenerator.generateId(), calendarName, specialDays, calendarId, token),
        requestConfig)
}

/**
 * Deletes a calendar asynchronously.
 *
 * @param calendarId The ID of the calendar to delete.
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.deleteCalendar(
    calendarId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<CalendarDeleted> {
    return sendCommand<DeleteCalendarMapi, CalendarDeleted>(
        Topics.Command.DELETE_CALENDAR,
        DeleteCalendarMapi(config.uuidGenerator.generateId(), calendarId, token),
        requestConfig)
}
