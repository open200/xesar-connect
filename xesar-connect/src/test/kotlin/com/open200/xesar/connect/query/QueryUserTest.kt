package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryUsers
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.User
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.UserFixture.userFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryUserTest :
    FunSpec({
        test("queryUserList without params") {
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
                                        userFixture,
                                        userFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                ),
                                            name = "lastname 2 String",
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
                val result = api.queryUsers()
                result.totalCount.shouldBeEqual(2)
                result.data[0].name.shouldBeEqual("lastname String")
                result.data[1].name.shouldBeEqual("lastname 2 String")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(User.QUERY_RESOURCE, requestId)
                    )
            }
        }
    })
