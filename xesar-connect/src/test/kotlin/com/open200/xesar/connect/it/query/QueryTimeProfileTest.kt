package com.open200.xesar.connect.it.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.queryTimeProfileById
import com.open200.xesar.connect.extension.queryTimeProfiles
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryTimeProfileTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryTimeProfileList without params") {
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
                                TimeProfile.QUERY_RESOURCE,
                                requestId,
                            )
                        )

                        val person =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            TimeProfileFixture.timeProfileFixture,
                                            TimeProfileFixture.timeProfileFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                    ),
                                                name = "name 2",
                                            ),
                                        ),
                                        2,
                                        2,
                                    ),
                                )
                            )

                        client
                            .publishAsync(Topics.Query.result(config.apiProperties.userId), person)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryTimeProfiles()
                    result.totalCount.shouldBeEqual(2)
                    result.data[0].name?.shouldBeEqual("name")
                    result.data[1].name?.shouldBeEqual("name 2")
                }
            }
        }

        test("queryTimeProfileById") {
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
                                TimeProfile.QUERY_RESOURCE,
                                requestId,
                                TimeProfileFixture.timeProfileFixture.id!!,
                            )
                        )

                        val person =
                            encodeQueryElement(
                                QueryElement(requestId, TimeProfileFixture.timeProfileFixture)
                            )

                        client
                            .publishAsync(Topics.Query.result(config.apiProperties.userId), person)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result =
                        api.queryTimeProfileById(TimeProfileFixture.timeProfileFixture.id!!)
                    result!!.id?.shouldBeEqual(TimeProfileFixture.timeProfileFixture.id!!)
                    result.name?.shouldBeEqual("name")
                }
            }
        }
    })
