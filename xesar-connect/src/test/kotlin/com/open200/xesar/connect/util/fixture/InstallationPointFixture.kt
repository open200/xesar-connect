package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.query.InstallationPoint
import com.open200.xesar.connect.messages.query.OnlineStatus
import java.util.*

object InstallationPointFixture {
    val installationPointFixture =
        InstallationPoint(
            id = UUID.fromString("7ca59670-bd30-4ea9-9bd1-2103a9bd2f2a"),
            name = "door 1 entry point",
            description = "door 1",
            installationId = "1a61d397-0afc-436a-8b75-521577d2aa02",
            installationType = "door 1",
            linkedInstallationPoints =
                listOf(
                    UUID.fromString("ed6236d0-a47c-46be-8495-d4755c38f103"),
                    UUID.fromString("7cbcddaa-50c3-48fb-8e5a-56bab47d8f81")),
            onlineStatus = OnlineStatus.connected,
            componentType = ComponentType.Cylinder,
            releaseDurationShort = 5,
            releaseDurationLong = 20,
            logMode = "dontSave",
            days = 30,
            manualOfficeMode = false,
            shopMode = false,
            openDoor = true,
            bleStatus = "NO_BLE",
        )
}
