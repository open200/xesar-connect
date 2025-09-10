package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.query.IdentificationMediumAccessData
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

object IdentificationMediumAccessDataFixture {

    val identificationMediumAccessData: IdentificationMediumAccessData =
        IdentificationMediumAccessData(
            identificationMedium =
                IdentificationMediumAccessData.MediumDetails(
                    masterKey = false,
                    mediumDataFrame = "sampleMediumDataFrame",
                    metadata =
                        IdentificationMediumAccessData.Metadata(
                            accessPoints =
                                listOf(
                                    IdentificationMediumAccessData.AccessPoint(
                                        accessDescription = "Main Entrance Door",
                                        bleMac = "00:1A:7D:DA:71:13",
                                        name = "FrontDoor",
                                    ),
                                    IdentificationMediumAccessData.AccessPoint(
                                        accessDescription = "Server Room Access",
                                        bleMac = "00:1B:44:11:3A:B7",
                                        name = "ServerRoom",
                                    ),
                                )
                        ),
                    officeMode = true,
                    t = "MobileIdentificationMedium",
                    transactionId = UUID.fromString("11111111-2222-3333-4444-555555555555"),
                    ts = OffsetDateTime.of(LocalDateTime.of(2025, 9, 9, 6, 10, 13), ZoneOffset.UTC),
                    validFrom =
                        OffsetDateTime.of(LocalDateTime.of(2024, 9, 9, 6, 10, 13), ZoneOffset.UTC),
                    validUntil =
                        OffsetDateTime.of(LocalDateTime.of(2026, 9, 9, 6, 10, 13), ZoneOffset.UTC),
                    version = 1,
                    xsId = "2be7e42003ef2b85f24cc7fb498b36a51cf145ecc472639bd9e8514bfb6d80b1",
                    xsMOBDK = "7878d5ab324008874a243d47bc079cb73eeeecef103a84b14d177fb16a7628c2",
                    xsMOBGID = "b20563b44cef137bb617c7cb04c3b99d19f4c71565b0a188081bc00cabdb2ca4",
                    xsMediumId = UUID.fromString("7c7347ba-e8f9-40ed-af7d-f08d9290a2ca"),
                    xsMobileId = UUID.fromString("ce1d491a-aa69-4488-905f-3ea48fd5b4af"),
                ),
            mediumType = IdentificationMediumAccessData.MediumType.SMARTPHONE,
            state = IdentificationMediumAccessData.State.UPDATE_COMPLETE,
        )
}
