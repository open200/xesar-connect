package com.open200.xesar.connect.it.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.queryEvvaComponentById
import com.open200.xesar.connect.extension.queryEvvaComponents
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.EvvaComponentFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryEvvaComponentTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryEvvaComponentList with params") {
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
                                EvvaComponent.QUERY_RESOURCE,
                                requestId,
                            )
                        )

                        val evvaComponentList =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            EvvaComponentFixture.evvaComponentFixture,
                                            EvvaComponentFixture.evvaComponentFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "f7019248-f7f9-4138-9af7-119e2e251408"
                                                    ),
                                                status = ComponentStatus.NotSynced,
                                            ),
                                        ),
                                        2,
                                        2,
                                    ),
                                )
                            )

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                evvaComponentList,
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryEvvaComponents()
                    result.totalCount.shouldBeEqual(2)
                    result.data[0].status?.shouldBeEqual(ComponentStatus.Synced)
                    result.data[1].status?.shouldBeEqual(ComponentStatus.NotSynced)
                }
            }
        }

        test("queryEvvaComponentById") {
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
                                EvvaComponent.QUERY_RESOURCE,
                                requestId,
                                EvvaComponentFixture.evvaComponentFixture.id,
                            )
                        )

                        val evvaComponent =
                            encodeQueryElement(
                                QueryElement(requestId, EvvaComponentFixture.evvaComponentFixture)
                            )

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                evvaComponent,
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result =
                        api.queryEvvaComponentById(EvvaComponentFixture.evvaComponentFixture.id)
                    result!!.id.shouldBeEqual(EvvaComponentFixture.evvaComponentFixture.id)
                    result.status?.shouldBeEqual(ComponentStatus.Synced)
                }
            }
        }
    })
