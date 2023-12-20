package com.open200.xesar.connect.query

import com.open200.xesar.connect.*
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.extension.queryIdentificationMediumByIdAsync
import com.open200.xesar.connect.extension.queryIdentificationMediumByMediumIdentifierAsync
import com.open200.xesar.connect.extension.queryIdentificationMediumListAsync
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.IdentificationMediumFixture
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.QueryTestHelper
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class QueryIdentificationMediumTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryIdentificationMediaList without params") {
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
                                IdentificationMedium.QUERY_RESOURCE, requestId))

                        val identificationMedium =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            IdentificationMediumFixture.identificationMediumFixture,
                                            IdentificationMediumFixture.identificationMediumFixture
                                                .copy(
                                                    id =
                                                        UUID.fromString(
                                                            "a4c838a8-f6be-49e0-abee-c1d3b2897279"),
                                                    label = "test door 2",
                                                )),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                identificationMedium)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryIdentificationMediumListAsync().await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0].label.shouldBeEqual("test door")
                        result.data[1].label.shouldBeEqual("test door 2")
                    }
                }
            }
        }

        test(
            "queryIdentificationMediumByMediumIdentifierAsync without params should return an element") {
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
                                "{\"resource\":\"identification-media\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":null,\"params\":{\"pageOffset\":null,\"pageLimit\":null,\"sort\":null,\"language\":null,\"filters\":[{\"field\":\"mediumIdentifier\",\"type\":\"eq\",\"value\":\"1\"}]}}")

                            val identificationMedium =
                                encodeQueryList(
                                    QueryList(
                                        requestId,
                                        QueryList.Response(
                                            listOf(
                                                IdentificationMediumFixture
                                                    .identificationMediumFixture),
                                            1,
                                            1,
                                        )))

                            client
                                .publishAsync(
                                    Topics.Query.result(config.apiProperties.userId),
                                    identificationMedium)
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        XesarConnectTestHelper.connect(config).use { api ->
                            api.subscribeAsync(
                                    Topics(Topics.Query.result(config.apiProperties.userId)))
                                .await()
                            val result =
                                api.queryIdentificationMediumByMediumIdentifierAsync(
                                        mediumIdentifierValue = 1)
                                    .await()

                            result?.label?.shouldBeEqual("test door")
                        }
                    }
                }
            }

        test(
            "queryIdentificationMediumByMediumIdentifierAsync without params should return null when no result") {
                val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
                coEvery { config.uuidGenerator.generateId() }.returns(requestId)
                runBlocking {
                    val simulatedBackendReady = CompletableDeferred<Unit>()
                    val queryReceived = CompletableDeferred<String?>()
                    launch {
                        XesarMqttClient.connectAsync(config).await().use { client ->
                            client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                            client.onMessage = { topic, payload ->
                                when (topic) {
                                    Topics.Query.REQUEST -> {
                                        queryReceived.complete(null)
                                    }
                                }
                            }

                            simulatedBackendReady.complete(Unit)

                            val queryContent = queryReceived.await()

                            queryContent.shouldBeNull()

                            val identificationMedium =
                                encodeQueryList(
                                    QueryList(
                                        requestId,
                                        QueryList.Response(
                                            listOf(),
                                            1,
                                            1,
                                        )))

                            client
                                .publishAsync(
                                    Topics.Query.result(config.apiProperties.userId),
                                    identificationMedium)
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        XesarConnectTestHelper.connect(config).use { api ->
                            api.subscribeAsync(
                                    Topics(Topics.Query.result(config.apiProperties.userId)))
                                .await()
                            val result =
                                api.queryIdentificationMediumByMediumIdentifierAsync(
                                        mediumIdentifierValue = 9999)
                                    .await()

                            result.shouldBeNull()
                        }
                    }
                }
            }

        test(
            "queryIdentificationMediumByMediumIdentifierAsync should throw an exception if the results is returning 2 values") {
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
                                "{\"resource\":\"identification-media\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":null,\"params\":{\"pageOffset\":null,\"pageLimit\":null,\"sort\":null,\"language\":null,\"filters\":[{\"field\":\"mediumIdentifier\",\"type\":\"eq\",\"value\":\"1\"}]}}")

                            val identificationMedium =
                                encodeQueryList(
                                    QueryList(
                                        requestId,
                                        QueryList.Response(
                                            listOf(
                                                IdentificationMediumFixture
                                                    .identificationMediumFixture,
                                                IdentificationMediumFixture
                                                    .identificationMediumFixture
                                                    .copy(
                                                        id =
                                                            UUID.fromString(
                                                                "a4c838a8-f6be-49e0-abee-c1d3b2897279"),
                                                        label = "test door 2",
                                                    )),
                                            2,
                                            2,
                                        )))

                            client
                                .publishAsync(
                                    Topics.Query.result(config.apiProperties.userId),
                                    identificationMedium)
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        XesarConnectTestHelper.connect(config).use { api ->
                            api.subscribeAsync(
                                    Topics(Topics.Query.result(config.apiProperties.userId)))
                                .await()

                            val result =
                                shouldThrow<MediumListSizeException> {
                                    api.queryIdentificationMediumByMediumIdentifierAsync(
                                            mediumIdentifierValue = 1)
                                        .await()
                                }

                            result.message?.shouldBeEqual(
                                "Expected exactly one element in the list with mediumIdentifier 1, but found 2 elements")
                        }
                    }
                }
            }

        test("queryIdentificationMediumById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            coEvery { config.uuidGenerator.generateId() }.returns(requestId)

            val id = IdentificationMediumFixture.identificationMediumFixture.id

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
                                IdentificationMedium.QUERY_RESOURCE, requestId, id))

                        val identificationMedium =
                            encodeQueryElement(
                                QueryElement(
                                    requestId,
                                    IdentificationMediumFixture.identificationMediumFixture))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                identificationMedium)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryIdentificationMediumByIdAsync(id).await()
                        result.id.shouldBeEqual(id)
                        result.label.shouldBeEqual("test door")
                    }
                }
            }
        }
    })
