package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryZoneById
import com.open200.xesar.connect.extension.queryZones
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.ZoneFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryZoneTest :
    FunSpec({
        test("queryZoneList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val zones =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        ZoneFixture.zoneFixture,
                                        ZoneFixture.zoneFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                ),
                                            name = "zone name2",
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), zones)
                }

                val api = harness.api
                val result = api.queryZones()
                result.totalCount.shouldBeEqual(2)
                result.data[0].name.shouldBeEqual("zone name")
                result.data[1].name.shouldBeEqual("zone name2")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(Zone.QUERY_RESOURCE, requestId)
                    )
            }
        }

        test("queryZoneById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val zone = encodeQueryElement(QueryElement(requestId, ZoneFixture.zoneFixture))

                    emitMessage(Topics.Query.result(userId), zone)
                }

                val api = harness.api
                val result = api.queryZoneById(ZoneFixture.zoneFixture.id)
                result!!.id.shouldBeEqual(ZoneFixture.zoneFixture.id)
                result.name.shouldBeEqual("zone name")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            Zone.QUERY_RESOURCE,
                            requestId,
                            ZoneFixture.zoneFixture.id,
                        )
                    )
            }
        }
    })
