package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.fixture.EvvaComponentFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class EvvaComponentElementTest :
    FunSpec({
        val evvaComponent =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"),
                EvvaComponentFixture.evvaComponentFixture)

        val evvaComponentString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\",\"response\":{\"componentType\":\"WallReader\",\"serialNumber\":null,\"upgradeMedia\":null,\"batteryCondition\":\"Full\",\"btbFirmwareVersion\":null,\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\",\"batteryStatusUpdatedAt\":\"2023-08-24T16:25:52.225991\",\"stateChangedAt\":\"2023-06-15T16:25:52.225991\",\"firmwareVersion\":null,\"status\":\"Synced\",\"bleMac\":null}}"

        test("encoding QueryElement for an evva component") {
            val evvaComponentEncoded = encodeQueryElement(evvaComponent)
            evvaComponentEncoded.shouldBeEqual(evvaComponentString)
        }

        test("decoding QueryElement for an evva component") {
            val evvaComponentDecoded = decodeQueryElement<EvvaComponent>(evvaComponentString)
            evvaComponentDecoded.shouldBe(evvaComponent)
        }
    })
