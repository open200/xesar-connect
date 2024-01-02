package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.deleteCalendar
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.CalendarDeleted
import com.open200.xesar.connect.messages.event.encodeEvent
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

class DeleteCalendarTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("delete calendar") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.DELETE_CALENDAR -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                CalendarDeleted(
                                    123, UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        client
                            .publishAsync(Topics.Event.CALENDAR_DELETED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(Topics(Topics.Event.CALENDAR_DELETED)).await()
                        val result =
                            api.deleteCalendar(
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                                .await()
                        result.id.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    }
                }
            }
        }
    })
