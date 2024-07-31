package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.CodingStation
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.util.fixture.CodingStationFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class CodingStationElementTest :
    FunSpec({
        val codingStation =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"),
                CodingStationFixture.codingStationFixture)

        val codingStationString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\"," +
                "\"response\":{" +
                "\"id\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"name\":\"coding station test\"," +
                "\"description\":\"a coding station description\"," +
                "\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\"," +
                "\"deleted\":null," +
                "\"online\":true}}"

        test("encoding QueryElement for a coding station") {
            val codingStationEncoded = encodeQueryElement(codingStation)
            codingStationEncoded.shouldBeEqual(codingStationString)
        }

        test("decoding QueryElement for a coding station") {
            val codingStationDecoded = decodeQueryElement<CodingStation>(codingStationString)
            codingStationDecoded.shouldBe(codingStation)
        }
    })
