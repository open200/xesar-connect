package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.fixture.IdentificationMediumAccessDataFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class IdentificationMediumAccessDataListTest :
    FunSpec({
        val identificationMediumAccessDataList =
            QueryList(
                UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                QueryList.Response(
                    listOf(
                        IdentificationMediumAccessDataFixture.identificationMediumAccessData,
                        IdentificationMediumAccessDataFixture.identificationMediumAccessData.copy(
                            identificationMedium =
                                IdentificationMediumAccessDataFixture.identificationMediumAccessData
                                    .identificationMedium
                                    .copy(
                                        xsMediumId =
                                            UUID.fromString("8d8347ba-e8f9-40ed-af7d-f08d9290b3db")
                                    )
                        ),
                    ),
                    2,
                    2,
                ),
            )

        val identificationMediumAccessDataString =
            "{\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\",\"response\":{\"data\":[{\"identificationMedium\":{\"masterKey\":false,\"mediumDataFrame\":\"sampleMediumDataFrame\",\"metadata\":{\"accessPoints\":[{\"accessDescription\":\"Main Entrance Door\",\"bleMac\":\"00:1A:7D:DA:71:13\",\"name\":\"FrontDoor\"},{\"accessDescription\":\"Server Room Access\",\"bleMac\":\"00:1B:44:11:3A:B7\",\"name\":\"ServerRoom\"}]},\"officeMode\":true,\"t\":\"MobileIdentificationMedium\",\"transactionId\":\"11111111-2222-3333-4444-555555555555\",\"ts\":\"2025-09-09T06:10:13Z\",\"validFrom\":\"2024-09-09T06:10:13Z\",\"validUntil\":\"2026-09-09T06:10:13Z\",\"version\":1,\"xsId\":\"2be7e42003ef2b85f24cc7fb498b36a51cf145ecc472639bd9e8514bfb6d80b1\",\"xsMOBDK\":\"7878d5ab324008874a243d47bc079cb73eeeecef103a84b14d177fb16a7628c2\",\"xsMOBGID\":\"b20563b44cef137bb617c7cb04c3b99d19f4c71565b0a188081bc00cabdb2ca4\",\"xsMediumId\":\"7c7347ba-e8f9-40ed-af7d-f08d9290a2ca\",\"xsMobileId\":\"ce1d491a-aa69-4488-905f-3ea48fd5b4af\"},\"mediumType\":\"SMARTPHONE\",\"state\":\"UPDATE_COMPLETE\"},{\"identificationMedium\":{\"masterKey\":false,\"mediumDataFrame\":\"sampleMediumDataFrame\",\"metadata\":{\"accessPoints\":[{\"accessDescription\":\"Main Entrance Door\",\"bleMac\":\"00:1A:7D:DA:71:13\",\"name\":\"FrontDoor\"},{\"accessDescription\":\"Server Room Access\",\"bleMac\":\"00:1B:44:11:3A:B7\",\"name\":\"ServerRoom\"}]},\"officeMode\":true,\"t\":\"MobileIdentificationMedium\",\"transactionId\":\"11111111-2222-3333-4444-555555555555\",\"ts\":\"2025-09-09T06:10:13Z\",\"validFrom\":\"2024-09-09T06:10:13Z\",\"validUntil\":\"2026-09-09T06:10:13Z\",\"version\":1,\"xsId\":\"2be7e42003ef2b85f24cc7fb498b36a51cf145ecc472639bd9e8514bfb6d80b1\",\"xsMOBDK\":\"7878d5ab324008874a243d47bc079cb73eeeecef103a84b14d177fb16a7628c2\",\"xsMOBGID\":\"b20563b44cef137bb617c7cb04c3b99d19f4c71565b0a188081bc00cabdb2ca4\",\"xsMediumId\":\"8d8347ba-e8f9-40ed-af7d-f08d9290b3db\",\"xsMobileId\":\"ce1d491a-aa69-4488-905f-3ea48fd5b4af\"},\"mediumType\":\"SMARTPHONE\",\"state\":\"UPDATE_COMPLETE\"}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryResponseElement for identification medium access data") {
            val identificationMediumAccessDataEncoded =
                encodeQueryList(identificationMediumAccessDataList)
            identificationMediumAccessDataEncoded.shouldBeEqual(
                identificationMediumAccessDataString
            )
        }

        test("decoding QueryResponseElement for identification medium access data") {
            val identificationMediumAccessDataEncoded =
                decodeQueryList<IdentificationMediumAccessData>(
                    identificationMediumAccessDataString
                )

            identificationMediumAccessDataEncoded.shouldBeEqual(identificationMediumAccessDataList)
        }
    })
