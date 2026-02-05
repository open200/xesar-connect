package com.open200.xesar.connect.encodingDecoding.event

import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.OffsetDateTime
import java.util.*

class SelfServiceModeEventsSerializationTest :
    FunSpec({
        val commandId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
        val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
        val transactionId = UUID.fromString("1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a")

        test("serialize SmartphoneLocked") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    SmartphoneLocked(
                        aggregateId = smartphoneId,
                        mediumIdentifier = 123L,
                        hasMasterKeyAccess = true,
                    ),
                )

            encodeEvent(apiEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                        "\"event\":{\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                        "\"installationPoints\":[]," +
                        "\"mediumIdentifier\":123," +
                        "\"authorizationProfileId\":null," +
                        "\"individualAuthorizations\":[]," +
                        "\"validityEnd\":null," +
                        "\"zones\":[]," +
                        "\"hasMasterKeyAccess\":true}}"
                )
        }

        test("deserialize SmartphoneLocked") {
            val text =
                "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                    "\"event\":{\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                    "\"installationPoints\":[]," +
                    "\"mediumIdentifier\":123," +
                    "\"individualAuthorizations\":[]," +
                    "\"zones\":[]," +
                    "\"hasMasterKeyAccess\":true}}"

            val result = decodeEvent<SmartphoneLocked>(text)

            result.event.aggregateId.shouldBeEqual(smartphoneId)
            result.event.mediumIdentifier.shouldBeEqual(123L)
            result.event.hasMasterKeyAccess.shouldBeEqual(true)
        }

        test("serialize SmartphoneRevokePending") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    SmartphoneRevokePending(mediumId = smartphoneId, transactionId = transactionId),
                )

            encodeEvent(apiEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                        "\"event\":{\"mediumId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                        "\"transactionId\":\"1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a\"}}"
                )
        }

        test("deserialize SmartphoneRevokePending") {
            val text =
                "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                    "\"event\":{\"mediumId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                    "\"transactionId\":\"1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a\"}}"

            decodeEvent<SmartphoneRevokePending>(text)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        SmartphoneRevokePending(
                            mediumId = smartphoneId,
                            transactionId = transactionId,
                        ),
                    )
                )
        }

        test("serialize SmartphoneUpdatePending") {
            val xsMediumId = UUID.fromString("ab3d5e7f-1234-5678-9abc-def012345678")
            val xsMobileId = UUID.fromString("cd4e6f8a-2345-6789-abcd-ef0123456789")
            val ts = OffsetDateTime.parse("2023-06-01T10:00:00+02:00")
            val validFrom = OffsetDateTime.parse("2023-06-01T00:00:00+02:00")
            val validUntil = OffsetDateTime.parse("2024-06-01T00:00:00+02:00")

            val apiEvent =
                ApiEvent(
                    commandId,
                    SmartphoneUpdatePending(
                        masterKey = false,
                        mediumDataFrame = "AABBCCDD",
                        metadata =
                            SmartphoneUpdatePending.Metadata(
                                accessPoints =
                                    listOf(
                                        SmartphoneUpdatePending.AccessPoint(
                                            accessDescription = "Main entrance",
                                            bleMac = "AA:BB:CC:DD:EE:FF",
                                            name = "Front Door",
                                        )
                                    )
                            ),
                        officeMode = true,
                        t = "smartphone",
                        transactionId = transactionId,
                        ts = ts,
                        validFrom = validFrom,
                        validUntil = validUntil,
                        version = 1,
                        xsId = "abc123hash",
                        xsMOBDK = "mobdkValue",
                        xsMOBGID = "mobgidValue",
                        xsMediumId = xsMediumId,
                        xsMobileId = xsMobileId,
                    ),
                )

            encodeEvent(apiEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                        "\"event\":{\"masterKey\":false," +
                        "\"mediumDataFrame\":\"AABBCCDD\"," +
                        "\"metadata\":{\"accessPoints\":[{\"accessDescription\":\"Main entrance\",\"bleMac\":\"AA:BB:CC:DD:EE:FF\",\"name\":\"Front Door\"}]}," +
                        "\"officeMode\":true," +
                        "\"t\":\"smartphone\"," +
                        "\"transactionId\":\"1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a\"," +
                        "\"ts\":\"2023-06-01T10:00:00+02:00\"," +
                        "\"validFrom\":\"2023-06-01T00:00:00+02:00\"," +
                        "\"validUntil\":\"2024-06-01T00:00:00+02:00\"," +
                        "\"version\":1," +
                        "\"xsId\":\"abc123hash\"," +
                        "\"xsMOBDK\":\"mobdkValue\"," +
                        "\"xsMOBGID\":\"mobgidValue\"," +
                        "\"xsMediumId\":\"ab3d5e7f-1234-5678-9abc-def012345678\"," +
                        "\"xsMobileId\":\"cd4e6f8a-2345-6789-abcd-ef0123456789\"}}"
                )
        }

        test("deserialize SmartphoneUpdatePending") {
            val xsMediumId = UUID.fromString("ab3d5e7f-1234-5678-9abc-def012345678")
            val ts = OffsetDateTime.parse("2023-06-01T10:00:00+02:00")
            val validFrom = OffsetDateTime.parse("2023-06-01T00:00:00+02:00")
            val validUntil = OffsetDateTime.parse("2024-06-01T00:00:00+02:00")

            val text =
                "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                    "\"event\":{\"masterKey\":false," +
                    "\"mediumDataFrame\":\"AABBCCDD\"," +
                    "\"metadata\":{\"accessPoints\":[{\"name\":\"Front Door\"}]}," +
                    "\"officeMode\":true," +
                    "\"transactionId\":\"1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a\"," +
                    "\"ts\":\"2023-06-01T10:00:00+02:00\"," +
                    "\"validFrom\":\"2023-06-01T00:00:00+02:00\"," +
                    "\"validUntil\":\"2024-06-01T00:00:00+02:00\"," +
                    "\"version\":1," +
                    "\"xsId\":\"abc123hash\"," +
                    "\"xsMediumId\":\"ab3d5e7f-1234-5678-9abc-def012345678\"}}"

            val result = decodeEvent<SmartphoneUpdatePending>(text)

            result.event.masterKey.shouldBeEqual(false)
            result.event.mediumDataFrame.shouldBeEqual("AABBCCDD")
            result.event.officeMode.shouldBeEqual(true)
            result.event.transactionId.shouldBeEqual(transactionId)
            result.event.ts.shouldBeEqual(ts)
            result.event.validFrom.shouldBeEqual(validFrom)
            result.event.validUntil.shouldBeEqual(validUntil)
            result.event.version.shouldBeEqual(1)
            result.event.xsId.shouldBeEqual("abc123hash")
            result.event.xsMediumId.shouldBeEqual(xsMediumId)
            result.event.metadata.accessPoints!![0].name.shouldBeEqual("Front Door")
        }
    })
