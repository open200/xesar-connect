package com.open200.xesar.connect.query

import com.open200.xesar.connect.*
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.extension.queryIdentificationMediumById
import com.open200.xesar.connect.extension.queryIdentificationMediumByMediumIdentifier
import com.open200.xesar.connect.extension.queryIdentificationMediums
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.IdentificationMediumFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryIdentificationMediumTest :
    FunSpec({
        test("queryIdentificationMediaList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val identificationMedium =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        IdentificationMediumFixture.identificationMediumFixture,
                                        IdentificationMediumFixture.identificationMediumFixture
                                            .copy(
                                                id =
                                                    UUID.fromString(
                                                        "a4c838a8-f6be-49e0-abee-c1d3b2897279"
                                                    ),
                                                label = "test door 2",
                                            ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), identificationMedium)
                }

                val api = harness.api
                val result = api.queryIdentificationMediums()
                result.totalCount.shouldBeEqual(2)
                result.data[0].label.shouldBeEqual("test door")
                result.data[1].label.shouldBeEqual("test door 2")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            IdentificationMedium.QUERY_RESOURCE,
                            requestId,
                        )
                    )
            }
        }

        test(
            "queryIdentificationMediumByMediumIdentifierAsync without params should return an element"
        ) {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val identificationMedium =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(IdentificationMediumFixture.identificationMediumFixture),
                                    1,
                                    1,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), identificationMedium)
                }

                val api = harness.api
                val result =
                    api.queryIdentificationMediumByMediumIdentifier(mediumIdentifierValue = 1)

                result?.label?.shouldBeEqual("test door")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        "{\"resource\":\"identification-media\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${MockedXesarConnect.TOKEN}\",\"id\":null,\"params\":{\"pageOffset\":null,\"pageLimit\":null,\"sort\":null,\"language\":null,\"filters\":[{\"field\":\"mediumIdentifier\",\"type\":\"eq\",\"value\":\"1\"}]}}"
                    )
            }
        }

        test(
            "queryIdentificationMediumByMediumIdentifierAsync without params should return null when no result"
        ) {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val identificationMedium =
                        encodeQueryList(QueryList(requestId, QueryList.Response(listOf(), 1, 1)))

                    emitMessage(Topics.Query.result(userId), identificationMedium)
                }

                val api = harness.api
                val result =
                    api.queryIdentificationMediumByMediumIdentifier(mediumIdentifierValue = 9999)

                result.shouldBeNull()

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        "{\"resource\":\"identification-media\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${MockedXesarConnect.TOKEN}\",\"id\":null,\"params\":{\"pageOffset\":null,\"pageLimit\":null,\"sort\":null,\"language\":null,\"filters\":[{\"field\":\"mediumIdentifier\",\"type\":\"eq\",\"value\":\"9999\"}]}}"
                    )
            }
        }

        test(
            "queryIdentificationMediumByMediumIdentifierAsync should throw an exception if the results is returning 2 values"
        ) {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val identificationMedium =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        IdentificationMediumFixture.identificationMediumFixture,
                                        IdentificationMediumFixture.identificationMediumFixture
                                            .copy(
                                                id =
                                                    UUID.fromString(
                                                        "a4c838a8-f6be-49e0-abee-c1d3b2897279"
                                                    ),
                                                label = "test door 2",
                                            ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), identificationMedium)
                }

                val api = harness.api
                val result =
                    shouldThrow<MediumListSizeException> {
                        api.queryIdentificationMediumByMediumIdentifier(mediumIdentifierValue = 1)
                    }

                result.message?.shouldBeEqual(
                    "Expected exactly one element in the list with mediumIdentifier 1, but found 2 elements"
                )

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        "{\"resource\":\"identification-media\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${MockedXesarConnect.TOKEN}\",\"id\":null,\"params\":{\"pageOffset\":null,\"pageLimit\":null,\"sort\":null,\"language\":null,\"filters\":[{\"field\":\"mediumIdentifier\",\"type\":\"eq\",\"value\":\"1\"}]}}"
                    )
            }
        }

        test("queryIdentificationMediumById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")

            val id = IdentificationMediumFixture.identificationMediumFixture.id
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val identificationMedium =
                        encodeQueryElement(
                            QueryElement(
                                requestId,
                                IdentificationMediumFixture.identificationMediumFixture,
                            )
                        )

                    emitMessage(Topics.Query.result(userId), identificationMedium)
                }

                val api = harness.api
                val result = api.queryIdentificationMediumById(id)
                result!!.id.shouldBeEqual(id)
                result.label.shouldBeEqual("test door")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            IdentificationMedium.QUERY_RESOURCE,
                            requestId,
                            id,
                        )
                    )
            }
        }
    })
