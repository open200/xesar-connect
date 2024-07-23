package com.open200.xesar.connect.it.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.queryInstallationPointById
import com.open200.xesar.connect.extension.queryInstallationPoints
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.InstallationPointFixture.installationPointFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryInstallationPointTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryInstallationPointList without params") {
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
                                InstallationPoint.QUERY_RESOURCE, requestId))

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
                                                        "a4c838a8-f6be-49e0-abee-c1d3b2897279"),
                                                name = "door 2 entry point",
                                                description = "door 2",
                                                installationId =
                                                    "0cefd48b-969e-43eb-aad6-98553288eb4d",
                                                installationType = "door 2",
                                            )),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), installationPoint)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryInstallationPoints()
                    result.totalCount.shouldBeEqual(2)
                    result.data[0].name.shouldBeEqual("door 1 entry point")
                    result.data[1].name.shouldBeEqual("door 2 entry point")
                }
            }
        }

        test("queryInstallationPointById") {
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
                                InstallationPoint.QUERY_RESOURCE,
                                requestId,
                                installationPointFixture.id))

                        val installationPoint =
                            encodeQueryElement(QueryElement(requestId, installationPointFixture))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), installationPoint)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryInstallationPointById(installationPointFixture.id)
                    result!!.id.shouldBeEqual(installationPointFixture.id)
                    result.name.shouldBeEqual("door 1 entry point")
                }
            }
        }
    })
