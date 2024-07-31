package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.query.CodingStation
import java.util.*

object CodingStationFixture {

    val codingStationFixture =
        CodingStation(
            id = UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
            partitionId = UUID.fromString("7b4399a0-21ce-4bee-ba43-e06e291248d2"),
            name = "coding station test",
            description = "a coding station description",
            online = true)
}
