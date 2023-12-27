package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.query.IdentificationMedium
import java.time.LocalDateTime
import java.util.*

object IdentificationMediumFixture {

    val identificationMediumFixture =
        IdentificationMedium(
            id = UUID.fromString("8293e920-90ce-48da-851c-cff54a13e2c6"),
            label = "test door",
            issuedAt = LocalDateTime.parse("2023-07-05T15:22:13.509825"),
            syncedAt = LocalDateTime.parse("2023-07-05T15:22:38.230076"),
            authorizationProfileId = UUID.fromString("d08fdd62-bc36-4e47-8bc9-62b603e75ed9"),
            authorizationProfileName = "Authorization Profile 1",
            validityDuration = 151,
            individualAuthorizationProfileIds =
                listOf(
                    UUID.fromString("3dba6935-6904-4bc0-99d3-8115c9bbbedc"),
                    UUID.fromString("66b9f5d9-5664-4bb9-9546-af315987752b")),
            mediumState = "ACTIVE",
            accessBeginAt = LocalDateTime.parse("2021-01-01T00:00:00"),
            accessEndAt = LocalDateTime.parse("2021-01-01T00:00:00"),
            validityBeginAt = LocalDateTime.parse("2021-01-01T00:00:00"),
            validityEndAt = LocalDateTime.parse("2021-01-01T00:00:00"),
            validityBeginAtInHardware = LocalDateTime.parse("2021-01-01T00:00:00"),
            validityEndAtInHardware = LocalDateTime.parse("2021-01-01T00:00:00"),
            external = false,
            disengagePeriod = DisengagePeriod.SHORT,
            mediumIdentifier = 1,
            outdated = true,
            personId = UUID.fromString("82a8a2cc-5d39-4cfa-b04e-49111a0bcdf7"),
            person = "test Person",
            hardwareId = "f45ebc226706800e1cf942c995d0a43db78ec563173fb79a43eedbfbd8f29222",
            nativeId = "046e0b8a967280",
            secure = true,
            softwareStatus = "ACTIVE",
            hardwareStatus = "ACTIVE",
            fitsOnHardware = true,
            userId = UUID.fromString("91781b22-ebdf-4ada-80cf-91f1fb9a4d96"),
            userName = "test User",
            requiredAction = "UPDATE",
            mediumType = "PASSIVE")
}
