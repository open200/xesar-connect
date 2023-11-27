package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.EvvaComponent
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.testutils.EvvaComponentFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class EvvaComponentListTest :
    FunSpec({
        val evvaComponentList =
            QueryList(
                UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
                QueryList.Response(
                    listOf(
                        EvvaComponentFixture.evvaComponentFixture,
                        EvvaComponentFixture.evvaComponentFixture.copy(
                            id = UUID.fromString("555e7d1a-54f1-432a-ade7-80d20a63ee2d")),
                    ),
                    2,
                    2))

        val evvaComponentString =
            "{\"requestId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\",\"response\":{\"data\":[{\"componentType\":\"WallReader\",\"batteryCondition\":\"Full\",\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\",\"batteryStatusUpdatedAt\":\"2023-08-24T16:25:52.225991\",\"stateChangedAt\":\"2023-06-15T16:25:52.225991\",\"status\":\"connected\"},{\"componentType\":\"WallReader\",\"batteryCondition\":\"Full\",\"id\":\"555e7d1a-54f1-432a-ade7-80d20a63ee2d\",\"batteryStatusUpdatedAt\":\"2023-08-24T16:25:52.225991\",\"stateChangedAt\":\"2023-06-15T16:25:52.225991\",\"status\":\"connected\"}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of evva components") {
            val evvaComponentEncoded = encodeQueryList(evvaComponentList)
            evvaComponentEncoded.shouldBeEqual(evvaComponentString)
        }

        test("decoding QueryList for a list of evva components") {
            val evvaComponentDecoded = decodeQueryList<EvvaComponent>(evvaComponentString)
            evvaComponentDecoded.shouldBe(evvaComponentList)
        }
    })
