package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.ExceptionTimeSerie
import com.open200.xesar.connect.messages.ExceptionTimepointSerie
import com.open200.xesar.connect.messages.TimePointSerie
import com.open200.xesar.connect.messages.TimeSerie
import com.open200.xesar.connect.messages.Weekday
import com.open200.xesar.connect.messages.query.OfficeMode
import java.time.LocalTime
import java.util.*

object OfficeModeFixture {

    val localTime = LocalTime.parse("14:15:00")

    val exceptionTimepointSerie =
        ExceptionTimepointSerie(
            calendars = listOf(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08")),
            points = listOf(localTime))

    val timeRange = TimeSerie.TimeRange(start = localTime, end = localTime)

    val timePointSerie = TimePointSerie(days = listOf(Weekday.MONDAY), points = listOf(localTime))

    val officeModeFixture =
        OfficeMode(
            timeProfileDetails = "timeProfileDetails",
            timeProfileName = "timeProfileName",
            exceptionTimePointSeries = listOf(exceptionTimepointSerie),
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"),
            installationPointId = UUID.fromString("39b25462-2580-44dc-b0a8-22fd6c03a023"),
            timeProfileId = UUID.fromString("6c791d61-3d3c-4f4f-a16f-2a2d2823ab40"),
            installationPointDescription = "installationPointDescription",
            installationPointName = "installationPointName",
            shopMode = true,
            timeSeries =
                listOf(TimeSerie(times = listOf(timeRange), days = listOf(Weekday.FRIDAY))),
            installationType = "installationType",
            manualOfficeMode = true,
            exceptionTimeSeries =
                listOf(
                    ExceptionTimeSerie(
                        times = listOf(timeRange),
                        calendars =
                            listOf(UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08")))),
            timePointSeries = listOf(timePointSerie))
}
