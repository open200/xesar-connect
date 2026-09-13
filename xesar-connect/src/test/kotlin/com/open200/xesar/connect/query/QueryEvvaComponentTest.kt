package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryEvvaComponentById
import com.open200.xesar.connect.extension.queryEvvaComponents
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.EvvaComponentFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryEvvaComponentTest :
    FunSpec({
        test("queryEvvaComponentList with params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val evvaComponentList =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        EvvaComponentFixture.evvaComponentFixture,
                                        EvvaComponentFixture.evvaComponentFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "f7019248-f7f9-4138-9af7-119e2e251408"
                                                ),
                                            status = ComponentStatus.NotSynced,
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), evvaComponentList)
                }

                val api = harness.api
                val result = api.queryEvvaComponents()
                result.totalCount.shouldBeEqual(2)
                result.data[0].status?.shouldBeEqual(ComponentStatus.Synced)
                result.data[1].status?.shouldBeEqual(ComponentStatus.NotSynced)

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(EvvaComponent.QUERY_RESOURCE, requestId)
                    )
            }
        }

        test("queryEvvaComponentById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val evvaComponent =
                        encodeQueryElement(
                            QueryElement(requestId, EvvaComponentFixture.evvaComponentFixture)
                        )

                    emitMessage(Topics.Query.result(userId), evvaComponent)
                }

                val api = harness.api
                val result =
                    api.queryEvvaComponentById(EvvaComponentFixture.evvaComponentFixture.id)
                result!!.id.shouldBeEqual(EvvaComponentFixture.evvaComponentFixture.id)
                result.status?.shouldBeEqual(ComponentStatus.Synced)

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            EvvaComponent.QUERY_RESOURCE,
                            requestId,
                            EvvaComponentFixture.evvaComponentFixture.id,
                        )
                    )
            }
        }
    })
