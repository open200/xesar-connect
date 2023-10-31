package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.messages.query.*
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
                            id = UUID.fromString("a4c838a8-f6be-49e0-abee-c1d3b2897279"))),
                    2,
                    2,
                ))

        val zoneString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"response\":{\"data\":[{\"installationPoint\":[{\"id\":\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\",\"name\":\"door 1 entry point\",\"description\":\"door 1\",\"installationId\":\"1a61d397-0afc-436a-8b75-521577d2aa02\",\"installationType\":\"door 1\",\"linkedInstallationPoints\":[\"ed6236d0-a47c-46be-8495-d4755c38f103\",\"7cbcddaa-50c3-48fb-8e5a-56bab47d8f81\"],\"onlineStatus\":\"offline\",\"componentType\":\"Cylinder\",\"releaseDurationShort\":5,\"releaseDurationLong\":20,\"logMode\":\"dontSave\",\"days\":30,\"manualOfficeMode\":false,\"shopMode\":false,\"openDoor\":true,\"bleStatus\":\"NO_BLE\",\"timeProfileName\":null,\"batteryCondition\":null}],\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"installationPointCount\":0,\"name\":\"zone name\",\"description\":\"zone description\",\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"},{\"installationPoint\":[{\"id\":\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\",\"name\":\"door 1 entry point\",\"description\":\"door 1\",\"installationId\":\"1a61d397-0afc-436a-8b75-521577d2aa02\",\"installationType\":\"door 1\",\"linkedInstallationPoints\":[\"ed6236d0-a47c-46be-8495-d4755c38f103\",\"7cbcddaa-50c3-48fb-8e5a-56bab47d8f81\"],\"onlineStatus\":\"offline\",\"componentType\":\"Cylinder\",\"releaseDurationShort\":5,\"releaseDurationLong\":20,\"logMode\":\"dontSave\",\"days\":30,\"manualOfficeMode\":false,\"shopMode\":false,\"openDoor\":true,\"bleStatus\":\"NO_BLE\",\"timeProfileName\":null,\"batteryCondition\":null}],\"partitionId\":\"7b4399a0-21ce-4bee-ba43-e06e291248d2\",\"installationPointCount\":0,\"name\":\"zone name\",\"description\":\"zone description\",\"id\":\"a4c838a8-f6be-49e0-abee-c1d3b2897279\"}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of zones") {
            val zoneEncoded = encodeQueryList(zoneList)
            logger.info(zoneEncoded)
            zoneEncoded.shouldBeEqual(zoneString)
        }

        test("decoding QueryList for a list of zones") {
            val zoneDecoded = decodeQueryList<Zone>(zoneString)
            zoneDecoded.shouldBe(zoneList)
        }
    })
