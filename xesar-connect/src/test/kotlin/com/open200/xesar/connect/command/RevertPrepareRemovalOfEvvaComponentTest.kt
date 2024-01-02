package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.revertPrepareRemovalOfEvvaComponent
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PrepareEvvaComponentRemovalReverted
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class RevertPrepareRemovalOfEvvaComponentTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("revert prepare removal of evva component") {
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
                                Topics.Command.REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT -> {
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
                                PrepareEvvaComponentRemovalReverted(
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    LocalDateTime.parse("2023-07-05T15:22:38.230076")))

                        client
                            .publishAsync(
                                Topics.Event.PREPARE_EVVA_COMPONENT_REMOVAL_REVERTED,
                                encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(
                                Topics(Topics.Event.PREPARE_EVVA_COMPONENT_REMOVAL_REVERTED))
                            .await()
                        val result =
                            api.revertPrepareRemovalOfEvvaComponent(
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                )
                                .await()
                        result.id.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                        result.stateChangedAt.shouldBeEqual(
                            LocalDateTime.parse("2023-07-05T15:22:38.230076"))
                    }
                }
            }
        }
    })
