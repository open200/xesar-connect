package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.TimeProfile
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class TimeProfileElementTest :
    FunSpec({
        val timeProfile =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"),
                TimeProfileFixture.timeProfileFixture,
            )

        val timeProfileString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\",\"response\":{\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"exceptionTimePointSeries\":[{\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"],\"points\":[\"14:15:00\"]}],\"name\":\"name\",\"description\":\"description\",\"timePointSeries\":[{\"days\":[\"MONDAY\"],\"points\":[\"14:15:00\"]}],\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\",\"type\":\"OFFICE_MODE\",\"validStandardTimeProfile\":true}}"

        test("encoding QueryElement for an office mode") {
            val timeProfileEncoded = encodeQueryElement(timeProfile)
            timeProfileEncoded.shouldBeEqual(timeProfileString)
        }

        test("decoding QueryElement for an office mode") {
            val timeProfileDecoded = decodeQueryElement<TimeProfile>(timeProfileString)
            timeProfileDecoded.shouldBe(timeProfile)
        }
    })
