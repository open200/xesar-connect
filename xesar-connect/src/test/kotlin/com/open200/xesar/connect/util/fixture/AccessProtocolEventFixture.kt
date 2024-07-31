package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.query.AccessProtocolEvent
import com.open200.xesar.connect.messages.query.EventType
import com.open200.xesar.connect.messages.query.GroupOfEvent
import java.time.LocalDateTime
import java.util.*

object AccessProtocolEventFixture {

    val eventValue =
        AccessProtocolEvent.EventValue(
            raw = "0000000000000000",
            officeModeAllowed = false,
            shopModeActivated = false,
            timestampFrom = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            fwVersionChanges = "v1.0",
            mediumIdentifier = 1234,
            errorCodes = "error codes",
            errorReaction = 1,
            initializationReason = 2,
            mediaRestrictedReason = 51,
            accessId = 1,
            timestampTo = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            errorModule = 3,
            inputState = 4,
            keyType = 5,
            mediaChangedReason = 19,
            componentType = ComponentType.Cylinder,
            inputNr = 6,
            nonceInHardware = 123456789L,
            fwUpdatePerformed = false,
            deltablacklistUid = 38,
            fwUpdateStatus = "success",
            nonceInSoftware = 987654321L,
            errorLocation = 7,
            startingUp = "starting up",
            errorNumber = "123",
            doorOpening = true,
            mediaUpgrade = true)

    val parameterMap =
        AccessProtocolEvent.ParameterMap(
            raw = "0000000000000000",
            keyType = "type",
            doorId = "door1",
            permanentOpeningAllowed = "true",
            shopModeActivated = "false")

    val installationPoint = UUID.fromString("d572db59-64f7-4853-b9f6-d3a218e1f4f4")

    val zone = UUID.fromString("dd51e0c8-29df-41e2-a68b-0697a965744d")

    val accessProtocolEvent =
        AccessProtocolEvent(
            id = UUID.fromString("0f0f5120-098c-4d8f-92f3-2a073b85ef8a"),
            entryIdentificator = 1,
            timestampComponentLocal = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            timestampUtc = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            receivedAt = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            eventType = EventType.NORMAL_OPENING,
            eventValue = eventValue,
            rawValue = "0000000000000000",
            parameterMap = parameterMap,
            installationPointId = installationPoint,
            installationPointName = "Door 1",
            installationPointIdentifier = "A01",
            zoneIds = listOf(zone, UUID.fromString("37afba11-94ad-41ed-a812-a05fb8239708")),
            accessId = 1,
            groupOfEvent = GroupOfEvent.MaintenanceComponent,
            eventNumber = 3074,
            identificationMediumId = UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
            identificationMediumLabel = "new Label",
            ttl = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            personId = UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
            person = "a new person")
}
