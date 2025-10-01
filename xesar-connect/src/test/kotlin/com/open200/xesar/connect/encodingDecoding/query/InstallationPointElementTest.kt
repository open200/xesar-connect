package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.fixture.InstallationPointFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class InstallationPointElementTest :
    FunSpec({
        val installationPoint =
            QueryElement(
                UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                InstallationPointFixture.installationPointFixture,
            )

        val installationPointString =
            "{\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\",\"response\":{\"id\":\"7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a\",\"name\":\"door 1 entry point\",\"description\":\"door 1\",\"installationId\":\"1a61d397-0afc-436a-8b75-521577d2aa02\",\"installationType\":\"door 1\",\"linkedInstallationPoints\":[\"ed6236d0-a47c-46be-8495-d4755c38f103\",\"7cbcddaa-50c3-48fb-8e5a-56bab47d8f81\"],\"onlineStatus\":\"connected\",\"componentType\":\"Cylinder\",\"releaseDurationShort\":5,\"releaseDurationLong\":20,\"logMode\":\"dontSave\",\"days\":30,\"manualOfficeMode\":false,\"shopMode\":false,\"openDoor\":true,\"bleStatus\":\"NO_BLE\",\"timeProfileName\":null,\"batteryCondition\":null,\"timeProfileId\":null,\"accessId\":null,\"secure\":null,\"bluetoothState\":null,\"entityMetadata\":[]}}"

        test("encoding QueryResponseElement for an installation Point") {
            val installationPointEncoded = encodeQueryElement(installationPoint)
            installationPointEncoded.shouldBeEqual(installationPointString)
        }

        test("decoding QueryResponseElement for an installation Point") {
            val installationPointsDecoded =
                decodeQueryElement<InstallationPoint>(installationPointString)

            installationPointsDecoded.shouldBeEqual(installationPoint)
        }
    })
