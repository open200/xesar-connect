package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.OfficeMode
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.util.fixture.OfficeModeFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class OfficeModeElementTest :
    FunSpec({
        val officeMode =
            QueryElement(
                UUID.fromString("d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6"),
                OfficeModeFixture.officeModeFixture)

        val officeModeString =
            "{\"requestId\":\"d385ab22-0f51-4b97-9ecd-b8ff3fd4fcb6\",\"response\":{\"timeProfileDetails\":\"timeProfileDetails\",\"timeProfileName\":\"timeProfileName\",\"exceptionTimePointSeries\":[{\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"],\"points\":[\"14:15:00\"]}],\"timeProfileId\":\"6c791d61-3d3c-4f4f-a16f-2a2d2823ab40\",\"installationPointDescription\":\"installationPointDescription\",\"installationPointName\":\"installationPointName\",\"shopMode\":true,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"FRIDAY\"]}],\"installationType\":\"installationType\",\"manualOfficeMode\":true,\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"installationPointId\":\"39b25462-2580-44dc-b0a8-22fd6c03a023\",\"timePointSeries\":[{\"days\":[\"MONDAY\"],\"points\":[\"14:15:00\"]}],\"id\":\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"}}"

        test("encoding QueryElement for an office mode") {
            val officeModeEncoded = encodeQueryElement(officeMode)
            officeModeEncoded.shouldBeEqual(officeModeString)
        }

        test("decoding QueryElement for an office mode") {
            val officeModeDecoded = decodeQueryElement<OfficeMode>(officeModeString)
            officeModeDecoded.shouldBe(officeMode)
        }
    })
