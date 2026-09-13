package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryAccessProtocolEvents
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.AccessProtocolEventFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryAccessProtocolEventTest :
    FunSpec({
        test("queryAccessProtocolEventList without params") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Query.REQUEST) {
                    val person =
                        encodeQueryList(
                            QueryList(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                QueryList.Response(
                                    listOf(
                                        AccessProtocolEventFixture.accessProtocolEvent,
                                        AccessProtocolEventFixture.accessProtocolEvent.copy(
                                            id =
                                                UUID.fromString(
                                                    "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                )
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
                val result = api.queryAccessProtocolEvents()
                result.totalCount.shouldBeEqual(2)
                result.data[0]
                    .id
                    .shouldBeEqual(UUID.fromString("0f0f5120-098c-4d8f-92f3-2a073b85ef8a"))
                result.data[1]
                    .id
                    .shouldBeEqual(UUID.fromString("4509ca29-9fd3-454f-9c98-fc0967fe3f66"))

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        "{\"resource\":\"access-protocol\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${MockedXesarConnect.TOKEN}\",\"id\":null,\"params\":null}"
                    )
            }
        }
    })
