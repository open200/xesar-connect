package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.messages.query.Zone
import java.util.*

object ZoneFixture {

    val zoneFixture =
        Zone(
            installationPoint = listOf(InstallationPointFixture.installationPointFixture),
            partitionId = UUID.fromString("7b4399a0-21ce-4bee-ba43-e06e291248d2"),
            installationPointCount = 0,
            name = "zone name",
            description = "zone description",
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"))
}
