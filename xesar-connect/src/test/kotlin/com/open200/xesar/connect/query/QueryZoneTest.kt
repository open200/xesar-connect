package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.queryZoneByIdAsync
import com.open200.xesar.connect.extension.queryZoneListAsync
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.QueryTestHelper
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import com.open200.xesar.connect.testutils.ZoneFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryZoneTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryZoneList without params") {
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
                            QueryTestHelper.createQueryRequest(Zone.QUERY_RESOURCE, requestId))

                        val zones =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            ZoneFixture.zoneFixture,
                                            ZoneFixture.zoneFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "4509ca29-9fd3-454f-9c98-fc0967fe3f66"),
                                                name = "zone name2",
                                            )),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(Topics.Query.result(config.apiProperties.userId), zones)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryZoneListAsync().await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0].name.shouldBeEqual("zone name")
                        result.data[1].name.shouldBeEqual("zone name2")
                    }
                }
            }
        }

        test("queryZoneById") {
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
                                Zone.QUERY_RESOURCE, requestId, ZoneFixture.zoneFixture.id))

                        val zone =
                            encodeQueryElement(QueryElement(requestId, ZoneFixture.zoneFixture))

                        client
                            .publishAsync(Topics.Query.result(config.apiProperties.userId), zone)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryZoneByIdAsync(ZoneFixture.zoneFixture.id).await()
                        result.id.shouldBeEqual(ZoneFixture.zoneFixture.id)
                        result.name.shouldBeEqual("zone name")
                    }
                }
            }
        }
    })
