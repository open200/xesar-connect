package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryOfficeModeById
import com.open200.xesar.connect.extension.queryOfficeModes
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.OfficeModeFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryOfficeModeTest :
    FunSpec({
        test("queryOfficeModeList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val officeModes =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        OfficeModeFixture.officeModeFixture,
                                        OfficeModeFixture.officeModeFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "894852cf-ca33-4734-a4a9-008eeeaeb005"
                                                ),
                                            timeProfileName = "timeProfileName2",
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), officeModes)
                }

                val api = harness.api
                val result = api.queryOfficeModes()
                result.totalCount.shouldBeEqual(2)
                result.data[0].timeProfileName.shouldBeEqual("timeProfileName")
                result.data[1].timeProfileName.shouldBeEqual("timeProfileName2")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(OfficeMode.QUERY_RESOURCE, requestId)
                    )
            }
        }

        test("queryOfficeModeById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val officeMode =
                        encodeQueryElement(
                            QueryElement(requestId, OfficeModeFixture.officeModeFixture)
                        )

                    emitMessage(Topics.Query.result(userId), officeMode)
                }

                val api = harness.api
                val result = api.queryOfficeModeById(OfficeModeFixture.officeModeFixture.id)
                result!!.id.shouldBeEqual(OfficeModeFixture.officeModeFixture.id)
                result.installationPointId.shouldBeEqual(
                    UUID.fromString("39b25462-2580-44dc-b0a8-22fd6c03a023")
                )

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            OfficeMode.QUERY_RESOURCE,
                            requestId,
                            OfficeModeFixture.officeModeFixture.id,
                        )
                    )
            }
        }
    })
