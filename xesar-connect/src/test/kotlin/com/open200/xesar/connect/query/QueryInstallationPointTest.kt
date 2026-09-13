package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryInstallationPointById
import com.open200.xesar.connect.extension.queryInstallationPoints
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.InstallationPointFixture.installationPointFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryInstallationPointTest :
    FunSpec({
        test("queryInstallationPointList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val installationPoint =
                        encodeQueryList(
                            QueryList(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                QueryList.Response(
                                    listOf(
                                        installationPointFixture,
                                        installationPointFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "a4c838a8-f6be-49e0-abee-c1d3b2897279"
                                                ),
                                            name = "door 2 entry point",
                                            description = "door 2",
                                            installationId = "0cefd48b-969e-43eb-aad6-98553288eb4d",
                                            installationType = "door 2",
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), installationPoint)
                }

                val api = harness.api
                val result = api.queryInstallationPoints()
                result.totalCount.shouldBeEqual(2)
                result.data[0].name.shouldBeEqual("door 1 entry point")
                result.data[1].name.shouldBeEqual("door 2 entry point")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            InstallationPoint.QUERY_RESOURCE,
                            requestId,
                        )
                    )
            }
        }

        test("queryInstallationPointById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val installationPoint =
                        encodeQueryElement(QueryElement(requestId, installationPointFixture))

                    emitMessage(Topics.Query.result(userId), installationPoint)
                }

                val api = harness.api
                val result = api.queryInstallationPointById(installationPointFixture.id)
                result!!.id.shouldBeEqual(installationPointFixture.id)
                result.name.shouldBeEqual("door 1 entry point")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            InstallationPoint.QUERY_RESOURCE,
                            requestId,
                            installationPointFixture.id,
                        )
                    )
            }
        }
    })
