package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.testutils.CalendarFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class CalendarElementTest :
    FunSpec({
        val calendar =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"),
                CalendarFixture.calendarFixture)

        val calendarString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\",\"response\":{\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"name\":\"string\",\"specialDays\":[\"2019-08-24\"],\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"}}"

        test("encoding QueryElement for a calendar") {
            val calendarEncoded = encodeQueryElement(calendar)
            calendarEncoded.shouldBeEqual(calendarString)
        }

        test("decoding QueryElement for a calendar") {
            val calendarDecoded = decodeQueryElement<Calendar>(calendarString)
            calendarDecoded.shouldBe(calendar)
        }
    })
