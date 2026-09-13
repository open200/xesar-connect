package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryPersonById
import com.open200.xesar.connect.extension.queryPersons
import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.PersonFixture.personFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryPersonTest :
    FunSpec({
        test("queryPersonList with params") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Query.REQUEST) {
                    val person =
                        encodeQueryList(
                            QueryList(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                QueryList.Response(
                                    listOf(
                                        personFixture,
                                        personFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "f7019248-f7f9-4138-9af7-119e2e251408"
                                                ),
                                            firstName = "firstname 2 String",
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), person)
                }

                val params =
                    Query.Params(
                        0,
                        3,
                        "firstName",
                        null,
                        listOf(
                            Query.Params.Filter(
                                "firstName",
                                FilterType.CONTAINS,
                                "filtered first name",
                            )
                        ),
                    )
                val api = harness.api
                val result = api.queryPersons(params)
                result.totalCount.shouldBeEqual(2)
                result.data[0].firstName.shouldBeEqual("firstname String")
                result.data[1].firstName.shouldBeEqual("firstname 2 String")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${MockedXesarConnect.TOKEN}\",\"id\":null," +
                            "\"params\":{\"pageOffset\":0,\"pageLimit\":3,\"sort\":\"firstName\",\"language\":null,\"filters\":[{\"field\":\"firstName\",\"type\":\"contains\",\"value\":\"filtered first name\"}]}}"
                    )
            }
        }

        test("queryPersonById") {
            runTest {
                val harness =
                    MockedXesarConnect(
                        this,
                        UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                    )
                harness.respondTo(Topics.Query.REQUEST) {
                    val person =
                        encodeQueryElement(
                            QueryElement(
                                UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                                personFixture,
                            )
                        )

                    emitMessage(Topics.Query.result(userId), person)
                }

                val api = harness.api
                val result = api.queryPersonById(personFixture.id)
                result!!.id.shouldBeEqual(personFixture.id)
                result.firstName.shouldBeEqual("firstname String")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\",\"token\":\"${MockedXesarConnect.TOKEN}\",\"id\":\"${personFixture.id}\",\"params\":null}"
                    )
            }
        }
    })
