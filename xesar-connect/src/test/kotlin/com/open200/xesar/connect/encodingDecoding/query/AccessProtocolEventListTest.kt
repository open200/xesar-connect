package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.AccessProtocolEvent
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.fixture.AccessProtocolEventFixture.accessProtocolEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class AccessProtocolEventListTest :
    FunSpec({
        val accessProtocolEventList =
            QueryList(
                UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
                QueryList.Response(
                    listOf(
                        accessProtocolEvent,
                        accessProtocolEvent.copy(
                            id = UUID.fromString("555e7d1a-54f1-432a-ade7-80d20a63ee2d")
                        ),
                    ),
                    2,
                    2,
                ),
            )

        val accessProtocolEventString =
            "{\"requestId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"response\":{\"data\":[{\"id\":\"0f0f5120-098c-4d8f-92f3-2a073b85ef8a\"," +
                "\"entryIdentificator\":1," +
                "\"timestampComponentLocal\":\"2023-06-15T16:25:52.225991\"," +
                "\"timestampUtc\":\"2023-06-15T16:25:52.225991\"," +
                "\"receivedAt\":\"2023-06-15T16:25:52.225991\"," +
                "\"eventType\":\"NORMAL_OPENING\"," +
                "\"eventValue\":{\"raw\":\"0000000000000000\"," +
                "\"officeModeAllowed\":false," +
                "\"shopModeActivated\":false," +
                "\"timestampFrom\":\"2023-06-15T16:25:52.225991\"," +
                "\"fwVersionChanges\":\"v1.0\"," +
                "\"mediumIdentifier\":1234," +
                "\"errorCodes\":\"error codes\"," +
                "\"errorReaction\":1," +
                "\"initializationReason\":2," +
                "\"mediaRestrictedReason\":51," +
                "\"accessId\":1," +
                "\"timestampTo\":\"2023-06-15T16:25:52.225991\"," +
                "\"errorModule\":3," +
                "\"inputState\":4," +
                "\"keyType\":5," +
                "\"mediaChangedReason\":19," +
                "\"componentType\":\"Cylinder\"," +
                "\"inputNr\":6," +
                "\"nonceInHardware\":123456789," +
                "\"fwUpdatePerformed\":false," +
                "\"deltablacklistUid\":38," +
                "\"fwUpdateStatus\":\"success\"," +
                "\"nonceInSoftware\":987654321," +
                "\"errorLocation\":7," +
                "\"startingUp\":\"starting up\"," +
                "\"errorNumber\":\"123\"," +
                "\"doorOpening\":true," +
                "\"mediaUpgrade\":true}," +
                "\"rawValue\":\"0000000000000000\"," +
                "\"parameterMap\":{}," +
                "\"installationPointId\":\"d572db59-64f7-4853-b9f6-d3a218e1f4f4\"," +
                "\"installationPointName\":\"Door 1\"," +
                "\"installationPointIdentifier\":\"A01\"," +
                "\"zoneIds\":[\"dd51e0c8-29df-41e2-a68b-0697a965744d\"," +
                "\"37afba11-94ad-41ed-a812-a05fb8239708\"]," +
                "\"accessId\":1," +
                "\"groupOfEvent\":\"MaintenanceComponent\"," +
                "\"eventNumber\":3074," +
                "\"identificationMediumId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"mediumIdentifier\":null," +
                "\"identificationMediumLabel\":\"new Label\"," +
                "\"ttl\":\"2023-06-15T16:25:52.225991\"," +
                "\"personId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"person\":\"a new person\"}," +
                "{\"id\":\"555e7d1a-54f1-432a-ade7-80d20a63ee2d\"," +
                "\"entryIdentificator\":1," +
                "\"timestampComponentLocal\":\"2023-06-15T16:25:52.225991\"," +
                "\"timestampUtc\":\"2023-06-15T16:25:52.225991\"," +
                "\"receivedAt\":\"2023-06-15T16:25:52.225991\"," +
                "\"eventType\":\"NORMAL_OPENING\"," +
                "\"eventValue\":{\"raw\":\"0000000000000000\"," +
                "\"officeModeAllowed\":false," +
                "\"shopModeActivated\":false," +
                "\"timestampFrom\":\"2023-06-15T16:25:52.225991\"," +
                "\"fwVersionChanges\":\"v1.0\"," +
                "\"mediumIdentifier\":1234," +
                "\"errorCodes\":\"error codes\"," +
                "\"errorReaction\":1," +
                "\"initializationReason\":2," +
                "\"mediaRestrictedReason\":51," +
                "\"accessId\":1," +
                "\"timestampTo\":\"2023-06-15T16:25:52.225991\"," +
                "\"errorModule\":3," +
                "\"inputState\":4," +
                "\"keyType\":5," +
                "\"mediaChangedReason\":19," +
                "\"componentType\":\"Cylinder\"," +
                "\"inputNr\":6," +
                "\"nonceInHardware\":123456789," +
                "\"fwUpdatePerformed\":false," +
                "\"deltablacklistUid\":38," +
                "\"fwUpdateStatus\":\"success\"," +
                "\"nonceInSoftware\":987654321," +
                "\"errorLocation\":7," +
                "\"startingUp\":\"starting up\"," +
                "\"errorNumber\":\"123\"," +
                "\"doorOpening\":true," +
                "\"mediaUpgrade\":true}," +
                "\"rawValue\":\"0000000000000000\"," +
                "\"parameterMap\":{}," +
                "\"installationPointId\":\"d572db59-64f7-4853-b9f6-d3a218e1f4f4\"," +
                "\"installationPointName\":\"Door 1\"," +
                "\"installationPointIdentifier\":\"A01\"," +
                "\"zoneIds\":[\"dd51e0c8-29df-41e2-a68b-0697a965744d\"," +
                "\"37afba11-94ad-41ed-a812-a05fb8239708\"]," +
                "\"accessId\":1," +
                "\"groupOfEvent\":\"MaintenanceComponent\"," +
                "\"eventNumber\":3074," +
                "\"identificationMediumId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"mediumIdentifier\":null," +
                "\"identificationMediumLabel\":\"new Label\"," +
                "\"ttl\":\"2023-06-15T16:25:52.225991\"," +
                "\"personId\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"person\":\"a new person\"}]," +
                "\"totalCount\":2," +
                "\"filterCount\":2}}"

        test("encoding QueryListElement for a list of authorization profiles") {
            val authorizationProfileEncoded = encodeQueryList(accessProtocolEventList)
            authorizationProfileEncoded.shouldBeEqual(accessProtocolEventString)
        }

        test("decoding QueryListElement for a list of authorization profiles") {
            val authorizationProfileDecoded =
                decodeQueryList<AccessProtocolEvent>(accessProtocolEventString)
            authorizationProfileDecoded.shouldBe(accessProtocolEventList)
        }
    })
