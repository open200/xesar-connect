package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.fixture.InstallationPointFixture.installationPointFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class InstallationPointListTest :
    FunSpec({
        val installationPointList =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        installationPointFixture,
                        installationPointFixture.copy(
                            id = UUID.fromString("a4c838a8-f6be-49e0-abee-c1d3b2897279"),
                            name = "door 2 entry point",
                            description = "door 2",
                            installationId = "0cefd48b-969e-43eb-aad6-98553288eb4d",
                            installationType = "door 2",
                        )),
                    2,
                    2,
                ))

        val installationPointString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"response\":{\"data\":[{\"id\":\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\",\"name\":\"door 1 entry point\",\"description\":\"door 1\",\"installationId\":\"1a61d397-0afc-436a-8b75-521577d2aa02\",\"installationType\":\"door 1\",\"linkedInstallationPoints\":[\"ed6236d0-a47c-46be-8495-d4755c38f103\",\"7cbcddaa-50c3-48fb-8e5a-56bab47d8f81\"],\"onlineStatus\":\"connected\",\"componentType\":\"Cylinder\",\"releaseDurationShort\":5,\"releaseDurationLong\":20,\"logMode\":\"dontSave\",\"days\":30,\"manualOfficeMode\":false,\"shopMode\":false,\"openDoor\":true,\"bleStatus\":\"NO_BLE\",\"timeProfileName\":null,\"batteryCondition\":null,\"timeProfileId\":null,\"accessId\":null,\"secure\":null,\"bluetoothState\":null},{\"id\":\"a4c838a8-f6be-49e0-abee-c1d3b2897279\",\"name\":\"door 2 entry point\",\"description\":\"door 2\",\"installationId\":\"0cefd48b-969e-43eb-aad6-98553288eb4d\",\"installationType\":\"door 2\",\"linkedInstallationPoints\":[\"ed6236d0-a47c-46be-8495-d4755c38f103\",\"7cbcddaa-50c3-48fb-8e5a-56bab47d8f81\"],\"onlineStatus\":\"connected\",\"componentType\":\"Cylinder\",\"releaseDurationShort\":5,\"releaseDurationLong\":20,\"logMode\":\"dontSave\",\"days\":30,\"manualOfficeMode\":false,\"shopMode\":false,\"openDoor\":true,\"bleStatus\":\"NO_BLE\",\"timeProfileName\":null,\"batteryCondition\":null,\"timeProfileId\":null,\"accessId\":null,\"secure\":null,\"bluetoothState\":null}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding installationPointList for a list of installation Points") {
            val installationPointEncoded = encodeQueryList(installationPointList)
            installationPointEncoded.shouldBeEqual(installationPointString)
        }

        test("decoding installationPointList for a list of installation Points") {
            val installationPointDecoded =
                decodeQueryList<InstallationPoint>(installationPointString)
            installationPointDecoded.shouldBe(installationPointList)
        }
    })
