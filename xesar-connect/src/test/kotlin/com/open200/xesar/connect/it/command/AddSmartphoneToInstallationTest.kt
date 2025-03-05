package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.addSmartphoneToInstallationAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.SmartphoneAddedToInstallation
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class AddSmartphoneToInstallationTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("add smartphone to installation") {
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
                                Topics.Command.ADD_SMARTPHONE_TO_INSTALLATION -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBe(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"accessBeginAt\":null,\"partitionId\":null,\"disengagePeriod\":null,\"authorizationProfileId\":null,\"individualAuthorizations\":[],\"messageLanguage\":null,\"label\":null,\"accessEndAt\":null,\"phoneNumber\":null,\"validityDuration\":null,\"personId\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                SmartphoneAddedToInstallation(
                                    accessBeginAt = LocalDateTime.parse("2024-08-08T12:25"),
                                    partitionId =
                                        UUID.fromString("5679d684-41c6-484c-8292-204feb085ef0"),
                                    disengagePeriod = DisengagePeriod.SHORT,
                                    mediumIdentifier = 8,
                                    authorizationProfileId =
                                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                                    individualAuthorizations = emptyList(),
                                    sagaId =
                                        UUID.fromString("a14d08c0-6f7a-4fc6-8469-be15a3e7b575"),
                                    messageLanguage = "en",
                                    label = "new f228c89d-c2e1-450d-8b79-89072b7b24c3",
                                    validityBeginAt = LocalDateTime.parse("2024-08-08T12:20"),
                                    accessEndAt = null,
                                    validityEndAt = LocalDateTime.parse("2024-12-09T14:20"),
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    issuedAt = LocalDateTime.parse("2024-08-08T12:29:55.708239786"),
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.SMARTPHONE_ADDED_TO_INSTALLATION,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.SMARTPHONE_ADDED_TO_INSTALLATION))
                        .await()
                    val result =
                        api.addSmartphoneToInstallationAsync(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            )
                            .await()

                    result.aggregateId.shouldBe(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                    )
                }
            }
        }
    })
