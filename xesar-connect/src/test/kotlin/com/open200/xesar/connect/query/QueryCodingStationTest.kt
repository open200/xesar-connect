package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.CodingStationFixture
import com.open200.xesar.connect.testutils.MosquittoContainer
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

class QueryCodingStationTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryCodingStationList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")

            coEvery { config.uuidGenerator.generateId() }.returns(requestId)
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
                                CodingStation.QUERY_RESOURCE, requestId))

                        val codingStation =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            CodingStationFixture.codingStationFixture,
                                            CodingStationFixture.codingStationFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "4509ca29-9fd3-454f-9c98-fc0967fe3f66"),
                                                partitionId =
                                                    UUID.fromString(
                                                        "6b4399a0-21ce-4bee-ba43-e06e291248d2"),
                                                online = false)),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), codingStation)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryCodingStationListAsync().await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0].online?.shouldBeEqual(true)
                        result.data[1].online?.shouldBeEqual(false)
                    }
                }
            }
        }

        test("queryCodingStationById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            coEvery { config.uuidGenerator.generateId() }.returns(requestId)

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
                                CodingStation.QUERY_RESOURCE,
                                requestId,
                                CodingStationFixture.codingStationFixture.id))

                        val codingStation =
                            encodeQueryElement(
                                QueryElement(requestId, CodingStationFixture.codingStationFixture))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), codingStation)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result =
                            api.queryCodingStationByIdAsync(
                                    CodingStationFixture.codingStationFixture.id)
                                .await()
                        result.id.shouldBeEqual(CodingStationFixture.codingStationFixture.id)
                        result.online?.shouldBeEqual(true)
                    }
                }
            }
        }
    })
