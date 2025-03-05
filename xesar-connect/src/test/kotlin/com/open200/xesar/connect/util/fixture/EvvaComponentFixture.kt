package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.query.BatteryCondition
import com.open200.xesar.connect.messages.query.ComponentStatus
import com.open200.xesar.connect.messages.query.EvvaComponent
import java.time.LocalDateTime
import java.util.*

object EvvaComponentFixture {

    val evvaComponentFixture =
        EvvaComponent(
            componentType = ComponentType.WallReader,
            batteryCondition = BatteryCondition.Full,
            id = UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08"),
            batteryStatusUpdatedAt = LocalDateTime.parse("2023-08-24T16:25:52.225991"),
            stateChangedAt = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            status = ComponentStatus.Synced,
        )
}
