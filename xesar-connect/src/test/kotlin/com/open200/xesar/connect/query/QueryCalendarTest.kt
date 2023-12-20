package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.queryCalendarByIdAsync
import com.open200.xesar.connect.extension.queryCalendarListAsync
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.testutils.CalendarFixture
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

class QueryCalendarTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryCalendarList without params") {
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
                            QueryTestHelper.createQueryRequest(Calendar.QUERY_RESOURCE, requestId))

                        val calendar =
                            encodeQueryList(
                                QueryList(
                                    requestId,
                                    QueryList.Response(
                                        listOf(
                                            CalendarFixture.calendarFixture,
                                            CalendarFixture.calendarFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "4509ca29-9fd3-454f-9c98-fc0967fe3f66"),
                                                partitionId =
                                                    UUID.fromString(
                                                        "6b4399a0-21ce-4bee-ba43-e06e291248d2"))),
                                        2,
                                        2,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), calendar)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result = api.queryCalendarListAsync().await()
                        result.totalCount.shouldBeEqual(2)
                        result.data[0]
                            .partitionId
                            .shouldBeEqual(UUID.fromString("7b4399a0-21ce-4bee-ba43-e06e291248d2"))
                        result.data[1]
                            .partitionId
                            .shouldBeEqual(UUID.fromString("6b4399a0-21ce-4bee-ba43-e06e291248d2"))
                    }
                }
            }
        }

        test("queryCalendarById") {
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
                                Calendar.QUERY_RESOURCE,
                                requestId,
                                CalendarFixture.calendarFixture.id))

                        val calendar =
                            encodeQueryElement(
                                QueryElement(requestId, CalendarFixture.calendarFixture))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId), calendar)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                            .await()
                        val result =
                            api.queryCalendarByIdAsync(CalendarFixture.calendarFixture.id).await()
                        result.id.shouldBeEqual(CalendarFixture.calendarFixture.id)
                        result.name.shouldBeEqual("string")
                    }
                }
            }
        }
    })
