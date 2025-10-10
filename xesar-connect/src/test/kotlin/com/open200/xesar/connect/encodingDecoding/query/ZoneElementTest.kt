package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.fixture.ZoneFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class ZoneElementTest :
    FunSpec({
        val zone =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"),
                ZoneFixture.zoneFixture,
            )

        val zoneString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\",\"response\":{\"installationPoints\":[\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\"],\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"installationPointCount\":0,\"name\":\"zone name\",\"description\":\"zone description\",\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\",\"entityMetadata\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"type\",\"value\":\"floor\"},{\"id\":\"0f8fad5b-d9cb-469f-a165-70867728950e\",\"name\":\"city\",\"value\":null}]}}"

        test("encoding QueryElement for a zone") {
            val zoneEncoded = encodeQueryElement(zone)
            logger.info(zoneString)
            zoneEncoded.shouldBeEqual(zoneString)
        }

        test("decoding QueryElement for a zone") {
            val zoneDecoded = decodeQueryElement<Zone>(zoneString)
            logger.info(zoneDecoded.toString())
            zoneDecoded.shouldBe(zone)
        }
    })
