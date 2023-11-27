package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.Zone
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.testutils.ZoneFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class ZoneListTest :
    FunSpec({
        val zoneList =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        ZoneFixture.zoneFixture,
                        ZoneFixture.zoneFixture.copy(
                            id = UUID.fromString("a4c838a8-f6be-49e0-abee-c1d3b2897279"),
                            installationPoints =
                                listOf(
                                    UUID.fromString("7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a"),
                                    UUID.fromString("f6a5bdf2-7c7d-11ee-b962-0242ac120002")))),
                    2,
                    2,
                ))

        val zoneString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"response\":{\"data\":[{\"installationPoints\":[\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\"],\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"installationPointCount\":0,\"name\":\"zone name\",\"description\":\"zone description\",\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"},{\"installationPoints\":[\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\",\"f6a5bdf2-7c7d-11ee-b962-0242ac120002\"],\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"installationPointCount\":0,\"name\":\"zone name\",\"description\":\"zone description\",\"id\":\"a4c838a8-f6be-49e0-abee-c1d3b2897279\"}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of zones") {
            val zoneEncoded = encodeQueryList(zoneList)
            zoneEncoded.shouldBeEqual(zoneString)
        }

        test("decoding QueryList for a list of zones") {
            val zoneDecoded = decodeQueryList<Zone>(zoneString)
            zoneDecoded.shouldBe(zoneList)
        }
    })
