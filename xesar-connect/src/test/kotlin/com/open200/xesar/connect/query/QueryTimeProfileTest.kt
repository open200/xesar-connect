package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryTimeProfileById
import com.open200.xesar.connect.extension.queryTimeProfiles
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryTimeProfileTest :
    FunSpec({
        test("queryTimeProfileList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val person =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        TimeProfileFixture.timeProfileFixture,
                                        TimeProfileFixture.timeProfileFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                ),
                                            name = "name 2",
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), person)
                }

                val api = harness.api
                val result = api.queryTimeProfiles()
                result.totalCount.shouldBeEqual(2)
                result.data[0].name?.shouldBeEqual("name")
                result.data[1].name?.shouldBeEqual("name 2")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(TimeProfile.QUERY_RESOURCE, requestId)
                    )
            }
        }

        test("queryTimeProfileById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val person =
                        encodeQueryElement(
                            QueryElement(requestId, TimeProfileFixture.timeProfileFixture)
                        )

                    emitMessage(Topics.Query.result(userId), person)
                }

                val api = harness.api
                val result = api.queryTimeProfileById(TimeProfileFixture.timeProfileFixture.id!!)
                result!!.id?.shouldBeEqual(TimeProfileFixture.timeProfileFixture.id!!)
                result.name?.shouldBeEqual("name")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            TimeProfile.QUERY_RESOURCE,
                            requestId,
                            TimeProfileFixture.timeProfileFixture.id!!,
                        )
                    )
            }
        }
    })
