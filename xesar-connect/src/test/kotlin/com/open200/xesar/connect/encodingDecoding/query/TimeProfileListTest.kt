package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.TimeProfile
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.testutils.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class TimeProfileListTest :
    FunSpec({
        val timeProfileList =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        TimeProfileFixture.timeProfileFixture,
                        TimeProfileFixture.timeProfileFixture.copy(
                            id = UUID.fromString("a4c838a8-f6be-49e0-abee-c1d3b2897279"))),
                    2,
                    2,
                ))

        val timeProfileString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"response\":{\"data\":[{\"timeSeries\":[{\"times\":[{\"start\":\"14:15\",\"end\":\"14:15\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15\",\"end\":\"14:15\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"exceptionTimePointSeries\":[{\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"],\"points\":[\"14:15\"]}],\"name\":\"name\",\"description\":\"description\",\"timePointSeries\":[{\"days\":[\"MONDAY\"],\"points\":[\"14:15\"]}],\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\",\"type\":\"OFFICE_MODE\",\"validStandardTimeProfile\":true},{\"timeSeries\":[{\"times\":[{\"start\":\"14:15\",\"end\":\"14:15\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15\",\"end\":\"14:15\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"exceptionTimePointSeries\":[{\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"],\"points\":[\"14:15\"]}],\"name\":\"name\",\"description\":\"description\",\"timePointSeries\":[{\"days\":[\"MONDAY\"],\"points\":[\"14:15\"]}],\"id\":\"a4c838a8-f6be-49e0-abee-c1d3b2897279\",\"type\":\"OFFICE_MODE\",\"validStandardTimeProfile\":true}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of time profiles") {
            val timeProfileEncoded = encodeQueryList(timeProfileList)
            timeProfileEncoded.shouldBeEqual(timeProfileString)
        }

        test("decoding QueryList for a list of time profiles") {
            val timeProfileDecoded = decodeQueryList<TimeProfile>(timeProfileString)
            timeProfileDecoded.shouldBe(timeProfileList)
        }
    })
