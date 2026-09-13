package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryCodingStationById
import com.open200.xesar.connect.extension.queryCodingStations
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.CodingStationFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryCodingStationTest :
    FunSpec({
        test("queryCodingStationList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val codingStation =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        CodingStationFixture.codingStationFixture,
                                        CodingStationFixture.codingStationFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                ),
                                            partitionId =
                                                UUID.fromString(
                                                    "6b4399a0-21ce-4bee-ba43-e06e291248d2"
                                                ),
                                            online = false,
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), codingStation)
                }

                val api = harness.api
                val result = api.queryCodingStations()
                result.totalCount.shouldBeEqual(2)
                result.data[0].online?.shouldBeEqual(true)
                result.data[1].online?.shouldBeEqual(false)

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(CodingStation.QUERY_RESOURCE, requestId)
                    )
            }
        }

        test("queryCodingStationById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val codingStation =
                        encodeQueryElement(
                            QueryElement(requestId, CodingStationFixture.codingStationFixture)
                        )

                    emitMessage(Topics.Query.result(userId), codingStation)
                }

                val api = harness.api
                val result =
                    api.queryCodingStationById(CodingStationFixture.codingStationFixture.id)
                result!!.id.shouldBeEqual(CodingStationFixture.codingStationFixture.id)
                result.online?.shouldBeEqual(true)

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            CodingStation.QUERY_RESOURCE,
                            requestId,
                            CodingStationFixture.codingStationFixture.id,
                        )
                    )
            }
        }
    })
