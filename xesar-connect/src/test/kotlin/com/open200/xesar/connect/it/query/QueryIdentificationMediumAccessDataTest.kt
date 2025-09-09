package com.open200.xesar.connect.it.query

import com.open200.xesar.connect.*
import com.open200.xesar.connect.exception.MediumListSizeException
import com.open200.xesar.connect.extension.queryIdentificationMediumAccessData
import com.open200.xesar.connect.extension.queryIdentificationMediumAccessDataById
import com.open200.xesar.connect.extension.queryIdentificationMediumAccessDataByState
import com.open200.xesar.connect.extension.queryIdentificationMediumById
import com.open200.xesar.connect.extension.queryIdentificationMediumByMediumIdentifier
import com.open200.xesar.connect.extension.queryIdentificationMediums
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.IdentificationMediumFixture
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

class QueryIdentificationMediumAccessDataTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryIdentificationMediaAccessDataList without params") {
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
                                IdentificationMediumAccessData.QUERY_RESOURCE,
                                requestId,
                            )
                        )

                        val IdentificationMediumAccessData =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            IdentificationMediumAccessDataFixture.identificationMediumAccessData,
                                            IdentificationMediumAccessDataFixture.identificationMediumAccessData.copy(
                                                identificationMedium = IdentificationMediumAccessDataFixture.identificationMediumAccessData.identificationMedium.copy(
                                                    xsMediumId = UUID.fromString("8d8347ba-e8f9-40ed-af7d-f08d9290b3db"),
                                                    xsMobileId = UUID.fromString("df2e491a-aa69-4488-905f-3ea48fd5c5b0")
                                                )),
                                        ),
                                        2,
                                        2,
                                    ),
                                )
                            )

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                IdentificationMediumAccessData,
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryIdentificationMediumAccessData()
                    result.totalCount.shouldBeEqual(2)
                    result.data[0].identificationMedium.xsMediumId
                        .shouldBeEqual(IdentificationMediumAccessDataFixture.identificationMediumAccessData.identificationMedium.xsMediumId)
                    result.data[1].identificationMedium.xsMediumId
                        .shouldBeEqual(UUID.fromString("8d8347ba-e8f9-40ed-af7d-f08d9290b3db"))

                    result.data[0].identificationMedium.xsMobileId
                        ?.shouldBeEqual(IdentificationMediumAccessDataFixture.identificationMediumAccessData.identificationMedium.xsMobileId as UUID)
                    result.data[1].identificationMedium.xsMobileId
                        ?.shouldBeEqual(UUID.fromString("df2e491a-aa69-4488-905f-3ea48fd5c5b0"))

                }
            }
        }

        test("queryIdentificationMediumAccessDataById without params") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            coEvery { config.uuidGenerator.generateId() }.returns(requestId)

            val id = IdentificationMediumAccessDataFixture.identificationMediumAccessData.identificationMedium.xsMediumId

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
                                IdentificationMediumAccessData.QUERY_RESOURCE,
                                requestId,
                                id,
                            )
                        )

                        val identificationMediumAccessData =
                            encodeQueryElement(
                                QueryElement(
                                    requestId,
                                    IdentificationMediumAccessDataFixture.identificationMediumAccessData,
                                )
                            )

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                identificationMediumAccessData,
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryIdentificationMediumAccessDataById(id)
                    result!!.identificationMedium.xsMediumId.shouldBeEqual(id)
                    result.identificationMedium.metadata.accessPoints?.get(1)?.bleMac?.shouldBeEqual("00:1B:44:11:3A:B7")
                }
            }
        }

        test("queryIdentificationMediumAccessDataByState with a given state") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            val stateToQuery = "UPDATE_COMPLETE"
            coEvery { config.uuidGenerator.generateId() }.returns(requestId)

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val queryReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            if (topic == Topics.Query.REQUEST) {
                                queryReceived.complete(payload.decodeToString())
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val queryContent = queryReceived.await()

                        queryContent.shouldBeEqual(
                            "{\"resource\":\"identification-media-access-data\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\",\"" +
                                "id\":null,\"params\":{\"pageOffset\":null,\"pageLimit\":null,\"sort\":null,\"language\":null,\"filters\":[{\"field\":\"state\",\"type\":\"eq\",\"value\":\"UPDATE_COMPLETE\"}]}}"
                        )

                        val mockResponseData = encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(IdentificationMediumAccessDataFixture.identificationMediumAccessData),
                                    1,
                                    1,
                                ),
                            )
                        )
                        client.publishAsync(
                            Topics.Query.result(config.apiProperties.userId),
                            mockResponseData,
                        ).await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId))).await()

                    val result = api.queryIdentificationMediumAccessDataByState(state = stateToQuery)

                    result.totalCount.shouldBeEqual(1)
                    result.data.size.shouldBeEqual(1)
                    result.data[0].identificationMedium.xsMediumId
                        .shouldBeEqual(IdentificationMediumAccessDataFixture.identificationMediumAccessData.identificationMedium.xsMediumId)
                }
            }
        }
    })
