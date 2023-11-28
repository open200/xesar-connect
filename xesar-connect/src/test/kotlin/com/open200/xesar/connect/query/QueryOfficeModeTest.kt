package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.OfficeModeFixture
import com.open200.xesar.connect.testutils.QueryTestHelper
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryOfficeModeTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryOfficeModeList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            coEvery { config.requestIdGenerator.generateId() }.returns(requestId)
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val queryReceived = CompletableDeferred<String>()
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Query.REQUEST -> {
                                    queryReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val queryContent = queryReceived.await()

                        queryContent.shouldBeEqual(
                            QueryTestHelper.createQueryRequest(
                                OfficeMode.QUERY_RESOURCE, requestId))

                        val officeModes =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            OfficeModeFixture.officeModeFixture,
                                            OfficeModeFixture.officeModeFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "894852cf-ca33-4734-a4a9-008eeeaeb005"),
                                                timeProfileName = "timeProfileName2",
                                            )),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), officeModes)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryOfficeModeListAsync().await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0].timeProfileName.shouldBeEqual("timeProfileName")
                        result.data[1].timeProfileName.shouldBeEqual("timeProfileName2")
                    }
                }
            }
        }

        test("queryOfficeModeById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            coEvery { config.requestIdGenerator.generateId() }.returns(requestId)

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val queryReceived = CompletableDeferred<String>()
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Query.REQUEST -> {
                                    queryReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val queryContent = queryReceived.await()

                        queryContent.shouldBeEqual(
                            QueryTestHelper.createQueryRequest(
                                OfficeMode.QUERY_RESOURCE,
                                requestId,
                                OfficeModeFixture.officeModeFixture.id))

                        val officeMode =
                            encodeQueryElement(
                                QueryElement(requestId, OfficeModeFixture.officeModeFixture))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), officeMode)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result =
                            api.queryOfficeModeByIdAsync(OfficeModeFixture.officeModeFixture.id)
                                .await()
                        result.id.shouldBeEqual(OfficeModeFixture.officeModeFixture.id)
                        result.installationPointId.shouldBeEqual(
                            UUID.fromString("39b25462-2580-44dc-b0a8-22fd6c03a023"))
                    }
                }
            }
        }
    })
