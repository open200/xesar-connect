package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.PersonFixture.personFixture
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryPersonTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryPersonList with params") {
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

                        queryContent.shouldBeEqual(
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":null," +
                                "\"params\":{\"pageOffset\":0,\"pageLimit\":3,\"sort\":\"firstName\",\"language\":null,\"filters\":[{\"field\":\"firstName\",\"type\":\"contains\",\"value\":\"filtered first name\"}]}}")

                        val person =
                            encodeQueryList(
                                QueryList(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    QueryList.Response(
                                        listOf(
                                            personFixture,
                                            personFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "f7019248-f7f9-4138-9af7-119e2e251408"),
                                                firstName = "firstname 2 String",
                                            )),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(Topics.Query.result(config.apiProperties.userId), person)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val params =
                        Query.Params(
                            0,
                            3,
                            "firstName",
                            null,
                            listOf(
                                Query.Params.Filter(
                                    "firstName",
                                    FilterType.CONTAINS,
                                    "filtered first name",
                                )))
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryPersonListAsync(params).await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0].firstName?.shouldBeEqual("firstname String")
                        result.data[1].firstName?.shouldBeEqual("firstname 2 String")
                    }
                }
            }
        }

        test("queryPersonById") {
            coEvery { config.requestIdGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"))

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
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":\"${personFixture.id}\",\"params\":null}")

                        val person =
                            encodeQueryElement(
                                QueryElement(
                                    UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                                    personFixture))

                        client
                            .publishAsync(Topics.Query.result(config.apiProperties.userId), person)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryPersonByIdAsync(personFixture.id).await()
                        result.id.shouldBeEqual(personFixture.id)
                        result.firstName?.shouldBeEqual("firstname String")
                    }
                }
            }
        }
    })
