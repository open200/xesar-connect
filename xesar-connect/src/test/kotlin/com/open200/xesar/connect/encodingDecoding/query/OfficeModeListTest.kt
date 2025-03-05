package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.OfficeMode
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.fixture.OfficeModeFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class OfficeModeListTest :
    FunSpec({
        val officeModePointList =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        OfficeModeFixture.officeModeFixture,
                        OfficeModeFixture.officeModeFixture.copy(
                            id = UUID.fromString("a4c838a8-f6be-49e0-abee-c1d3b2897279")
                        ),
                    ),
                    2,
                    2,
                ),
            )

        val officeModeString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"response\":{\"data\":[{\"timeProfileDetails\":\"timeProfileDetails\",\"timeProfileName\":\"timeProfileName\",\"exceptionTimePointSeries\":[{\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"],\"points\":[\"14:15:00\"]}],\"timeProfileId\":\"6c791d61-3d3c-4f4f-a16f-2a2d2823ab40\",\"installationPointDescription\":\"installationPointDescription\",\"installationPointName\":\"installationPointName\",\"shopMode\":true,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"FRIDAY\"]}],\"installationType\":\"installationType\",\"manualOfficeMode\":true,\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"installationPointId\":\"39b25462-2580-44dc-b0a8-22fd6c03a023\",\"timePointSeries\":[{\"days\":[\"MONDAY\"],\"points\":[\"14:15:00\"]}],\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"},{\"timeProfileDetails\":\"timeProfileDetails\",\"timeProfileName\":\"timeProfileName\",\"exceptionTimePointSeries\":[{\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"],\"points\":[\"14:15:00\"]}],\"timeProfileId\":\"6c791d61-3d3c-4f4f-a16f-2a2d2823ab40\",\"installationPointDescription\":\"installationPointDescription\",\"installationPointName\":\"installationPointName\",\"shopMode\":true,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"FRIDAY\"]}],\"installationType\":\"installationType\",\"manualOfficeMode\":true,\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"installationPointId\":\"39b25462-2580-44dc-b0a8-22fd6c03a023\",\"timePointSeries\":[{\"days\":[\"MONDAY\"],\"points\":[\"14:15:00\"]}],\"id\":\"a4c838a8-f6be-49e0-abee-c1d3b2897279\"}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryList for a list of office modes") {
            val officeModeEncoded = encodeQueryList(officeModePointList)
            officeModeEncoded.shouldBeEqual(officeModeString)
        }

        test("decoding QueryList for a list of office modes") {
            val officeModeDecoded = decodeQueryList<OfficeMode>(officeModeString)
            officeModeDecoded.shouldBe(officeModePointList)
        }
    })
