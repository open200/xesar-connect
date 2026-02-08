package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.query.Zone
import java.util.*

object ZoneFixture {

    val zoneFixture =
        Zone(
            partitionId = UUID.fromString("7b4399a0-21ce-4bee-ba43-e06e291248d2"),
            installationPointCount = 0,
            name = "zone name",
            description = "zone description",
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"),
            installationPoints = listOf(UUID.fromString("7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a")),
            entityMetadata =
                listOf(
                    EntityMetadata(
                        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                        name = "type",
                        value = "floor",
                    ),
                    EntityMetadata(
                        id = UUID.fromString("0f8fad5b-d9cb-469f-a165-70867728950e"),
                        name = "city",
                        value = null,
                    ),
                ),
        )
}
