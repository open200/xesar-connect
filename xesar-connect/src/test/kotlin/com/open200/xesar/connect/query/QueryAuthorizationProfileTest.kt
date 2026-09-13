package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryAuthorizationProfileById
import com.open200.xesar.connect.extension.queryAuthorizationProfiles
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.AuthorizationProfileFixture.authorizationProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryAuthorizationProfileTest :
    FunSpec({
        test("queryAuthorizationProfileList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val authorizationProfiles =
                        encodeQueryList(
                            QueryList(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                QueryList.Response(
                                    listOf(
                                        authorizationProfileFixture,
                                        authorizationProfileFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "894852cf-ca33-4734-a4a9-008eeeaeb005"
                                                ),
                                            name = "authorization profile 2",
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), authorizationProfiles)
                }

                val api = harness.api
                val result = api.queryAuthorizationProfiles()
                result.totalCount.shouldBeEqual(2)
                result.data[0].name.shouldBeEqual("authorization profile 1 String")
                result.data[1].name.shouldBeEqual("authorization profile 2")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            AuthorizationProfile.QUERY_RESOURCE,
                            requestId,
                        )
                    )
            }
        }

        test("queryAuthorizationProfileById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val person =
                        encodeQueryElement(
                            QueryElement(
                                UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                                authorizationProfileFixture,
                            )
                        )

                    emitMessage(Topics.Query.result(userId), person)
                }

                val api = harness.api
                val result = api.queryAuthorizationProfileById(authorizationProfileFixture.id)
                result!!.id.shouldBeEqual(authorizationProfileFixture.id)
                result.name.shouldBeEqual("authorization profile 1 String")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            AuthorizationProfile.QUERY_RESOURCE,
                            requestId,
                            authorizationProfileFixture.id,
                        )
                    )
            }
        }
    })
