package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.query.Calendar
import java.time.LocalDate
import java.util.*

object CalendarFixture {

    val calendarFixture =
        Calendar(
            partitionId = UUID.fromString("7b4399a0-21ce-4bee-ba43-e06e291248d2"),
            name = "string",
            specialDays = listOf(LocalDate.parse("2019-08-24")),
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"))
}
