package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.testutils.CalendarFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class CalendarListTest :
    FunSpec({
        val calendarList =
            QueryList(
                UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
                QueryList.Response(
                    listOf(
                        CalendarFixture.calendarFixture,
                        CalendarFixture.calendarFixture.copy(
                            id = UUID.fromString("555e7d1a-54f1-432a-ade7-80d20a63ee2d")),
                    ),
                    2,
                    2))

        val calendarString =
            "{\"requestId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\",\"response\":{\"data\":[{\"serialVersionUID\":0,\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"name\":\"string\",\"specialDays\":[\"2019-08-24\"],\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"},{\"serialVersionUID\":0,\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"name\":\"string\",\"specialDays\":[\"2019-08-24\"],\"id\":\"555e7d1a-54f1-432a-ade7-80d20a63ee2d\"}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of calendars") {
            val calendarEncoded = encodeQueryList(calendarList)
            calendarEncoded.shouldBeEqual(calendarString)
        }

        test("decoding QueryList for a list of calendars") {
            val calendarDecoded = decodeQueryList<Calendar>(calendarString)
            calendarDecoded.shouldBe(calendarList)
        }
    })
