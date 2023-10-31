package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.Zone
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.testutils.ZoneFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class ZoneElementTest :
    FunSpec({
        val zone =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"), ZoneFixture.zoneFixture)

        val zoneString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\",\"response\":{\"installationPoint\":[{\"id\":\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\",\"name\":\"door 1 entry point\",\"description\":\"door 1\",\"installationId\":\"1a61d397-0afc-436a-8b75-521577d2aa02\",\"installationType\":\"door 1\",\"linkedInstallationPoints\":[\"ed6236d0-a47c-46be-8495-d4755c38f103\",\"7cbcddaa-50c3-48fb-8e5a-56bab47d8f81\"],\"onlineStatus\":\"offline\",\"componentType\":\"Cylinder\",\"releaseDurationShort\":5,\"releaseDurationLong\":20,\"logMode\":\"dontSave\",\"days\":30,\"manualOfficeMode\":false,\"shopMode\":false,\"openDoor\":true,\"bleStatus\":\"NO_BLE\",\"timeProfileName\":null,\"batteryCondition\":null}],\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"installationPointCount\":0,\"name\":\"zone name\",\"description\":\"zone description\",\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"}}"

        test("encoding QueryElement for a zone") {
            val zoneEncoded = encodeQueryElement(zone)
            zoneEncoded.shouldBeEqual(zoneString)
        }

        test("decoding QueryElement for a zone") {
            val zoneDecoded = decodeQueryElement<Zone>(zoneString)
            zoneDecoded.shouldBe(zone)
        }
    })
