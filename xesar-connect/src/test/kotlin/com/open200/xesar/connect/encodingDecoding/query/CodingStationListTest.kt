package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.CodingStation
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.testutils.CodingStationFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class CodingStationListTest :
    FunSpec({
        val codingStationList =
            QueryList(
                UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
                QueryList.Response(
                    listOf(
                        CodingStationFixture.codingStationFixture,
                        CodingStationFixture.codingStationFixture.copy(
                            id = UUID.fromString("555e7d1a-54f1-432a-ade7-80d20a63ee2d")),
                    ),
                    2,
                    2))

        val codingStationString =
            "{\"requestId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\",\"response\":{\"data\":[{\"id\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\",\"name\":\"coding station test\",\"description\":\"a coding station description\",\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"deleted\":null,\"online\":true},{\"id\":\"555e7d1a-54f1-432a-ade7-80d20a63ee2d\",\"name\":\"coding station test\",\"description\":\"a coding station description\",\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"deleted\":null,\"online\":true}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of coding stations") {
            val codingStationEncoded = encodeQueryList(codingStationList)
            codingStationEncoded.shouldBeEqual(codingStationString)
        }

        test("decoding QueryList for a list of coding stations") {
            val codingStationDecoded = decodeQueryList<CodingStation>(codingStationString)
            codingStationDecoded.shouldBe(codingStationList)
        }
    })
