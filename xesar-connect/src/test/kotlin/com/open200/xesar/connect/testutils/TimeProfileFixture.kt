package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.messages.*
import com.open200.xesar.connect.messages.query.TimeProfile
import com.open200.xesar.connect.messages.query.TimeProfileType
import java.time.LocalTime
import java.util.*

object TimeProfileFixture {

    val localTime = LocalTime.parse("14:15")
    val timeRange = TimeSerie.TimeRange(start = localTime, end = localTime)

    val exceptionTimeSerie =
        listOf(
            ExceptionTimeSerie(
                times = listOf(timeRange),
                calendars = listOf(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"))))

    val exceptionTimepointSerie =
        ExceptionTimepointSerie(
            calendars = listOf(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08")),
            points = listOf(localTime))

    val timePointSerie =
        listOf(TimePointSerie(days = listOf(Weekday.MONDAY), points = listOf(localTime)))

    val timeSeries = listOf(TimeSerie(times = listOf(timeRange), days = listOf(Weekday.MONDAY)))

    val timeProfileFixture =
        TimeProfile(
            timeSeries = timeSeries,
            exceptionTimeSeries = exceptionTimeSerie,
            exceptionTimePointSeries = listOf(exceptionTimepointSerie),
            name = "name",
            description = "description",
            timePointSeries = timePointSerie,
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"),
            type = TimeProfileType.OFFICE_MODE,
            validStandardTimeProfile = true)
}
