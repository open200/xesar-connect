package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.InstallationPointFixture.installationPointFixture
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
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
            coEvery { config.requestIdGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
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

                        logger.info("queryContent: $queryContent")

                        queryContent.shouldBeEqual(
                            "{\"resource\":\"installation-points\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":null,\"params\":null}")

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

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryInstallationPointListAsync().await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0].name?.shouldBeEqual("door 1 entry point")
                        result.data[1].name?.shouldBeEqual("door 2 entry point")
                    }
                }
            }
        }

        test("queryInstallationPointById") {
            coEvery { config.requestIdGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"))

            val id = installationPointFixture.id

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
                            "{\"resource\":\"installation-points\",\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":\"$id\",\"params\":null}")

                        val installationPoint =
                            encodeQueryElement(
                                QueryElement(
                                    UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                                    installationPointFixture))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), installationPoint)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryInstallationPointByIdAsync(id).await()
                        result.id.shouldBeEqual(id)
                        result.name?.shouldBeEqual("door 1 entry point")
                    }
                }
            }
        }
    })
