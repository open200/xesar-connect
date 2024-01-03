package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.messages.*
import com.open200.xesar.connect.messages.query.*
import java.time.LocalTime
import java.util.*

object TimeProfileFixture {

    val localTime = LocalTime.parse("14:15")
    val timeRange = TimeSerie.TimeRange(start = localTime, end = localTime)

    val exceptionTimeSerie =
        ExceptionTimeSerie(
            times = listOf(timeRange),
            calendars = listOf(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08")))

    val exceptionTimepointSerie =
        ExceptionTimepointSerie(
            calendars = listOf(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08")),
            points = listOf(localTime))

    val timePointSerie = TimePointSerie(days = listOf(Weekday.MONDAY), points = listOf(localTime))

    val timeProfileFixture =
        TimeProfile(
            timeSeries =
                listOf(TimeSerie(times = listOf(timeRange), days = listOf(Weekday.MONDAY))),
            exceptionTimeSeries = listOf(exceptionTimeSerie),
            exceptionTimePointSeries = listOf(exceptionTimepointSerie),
            name = "name",
            description = "description",
            timePointSeries = listOf(timePointSerie),
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"),
            type = TimeProfileType.OFFICE_MODE,
            validStandardTimeProfile = true)
}
