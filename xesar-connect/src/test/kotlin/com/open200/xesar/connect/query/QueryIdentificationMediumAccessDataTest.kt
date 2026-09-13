package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryIdentificationMediumAccessData
import com.open200.xesar.connect.extension.queryIdentificationMediumAccessDataById
import com.open200.xesar.connect.messages.query.IdentificationMediumAccessData
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.IdentificationMediumAccessDataFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryIdentificationMediumAccessDataTest :
    FunSpec({
        test("queryIdentificationMediaAccessDataList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val IdentificationMediumAccessData =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        IdentificationMediumAccessDataFixture
                                            .identificationMediumAccessData,
                                        IdentificationMediumAccessDataFixture
                                            .identificationMediumAccessData
                                            .copy(
                                                identificationMedium =
                                                    IdentificationMediumAccessDataFixture
                                                        .identificationMediumAccessData
                                                        .identificationMedium
                                                        .copy(
                                                            xsMediumId =
                                                                UUID.fromString(
                                                                    "8d8347ba-e8f9-40ed-af7d-f08d9290b3db"
                                                                ),
                                                            xsMobileId =
                                                                UUID.fromString(
                                                                    "df2e491a-aa69-4488-905f-3ea48fd5c5b0"
                                                                ),
                                                        )
                                            ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), IdentificationMediumAccessData)
                }

                val api = harness.api
                val result = api.queryIdentificationMediumAccessData()
                result.totalCount.shouldBeEqual(2)
                result.data[0]
                    .identificationMedium
                    .xsMediumId
                    .shouldBeEqual(
                        IdentificationMediumAccessDataFixture.identificationMediumAccessData
                            .identificationMedium
                            .xsMediumId
                    )
                result.data[1]
                    .identificationMedium
                    .xsMediumId
                    .shouldBeEqual(UUID.fromString("8d8347ba-e8f9-40ed-af7d-f08d9290b3db"))

                result.data[0]
                    .identificationMedium
                    .xsMobileId
                    ?.shouldBeEqual(
                        IdentificationMediumAccessDataFixture.identificationMediumAccessData
                            .identificationMedium
                            .xsMobileId as UUID
                    )
                result.data[1]
                    .identificationMedium
                    .xsMobileId
                    ?.shouldBeEqual(UUID.fromString("df2e491a-aa69-4488-905f-3ea48fd5c5b0"))

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            IdentificationMediumAccessData.QUERY_RESOURCE,
                            requestId,
                        )
                    )
            }
        }

        test("queryIdentificationMediumAccessDataById without params") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")

            val id =
                IdentificationMediumAccessDataFixture.identificationMediumAccessData
                    .identificationMedium
                    .xsMediumId
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val identificationMediumAccessData =
                        encodeQueryElement(
                            QueryElement(
                                requestId,
                                IdentificationMediumAccessDataFixture.identificationMediumAccessData,
                            )
                        )

                    emitMessage(Topics.Query.result(userId), identificationMediumAccessData)
                }

                val api = harness.api
                val result = api.queryIdentificationMediumAccessDataById(id)
                result!!.identificationMedium.xsMediumId.shouldBeEqual(id)
                result.identificationMedium.metadata.accessPoints
                    ?.get(1)
                    ?.bleMac
                    ?.shouldBeEqual("00:1B:44:11:3A:B7")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            IdentificationMediumAccessData.QUERY_RESOURCE,
                            requestId,
                            id,
                        )
                    )
            }
        }
    })
