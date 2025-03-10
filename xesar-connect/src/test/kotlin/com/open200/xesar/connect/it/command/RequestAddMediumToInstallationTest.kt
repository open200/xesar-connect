package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.requestToAddMediumToInstallationAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.*
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class RequestAddMediumToInstallationTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("request add medium to installation returning both events") {
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
                                Topics.Command.REQUEST_ADD_MEDIUM_TO_INSTALLATION -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"hardwareId\":\"hardwareId\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"terminalId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"label\":null,\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AddMediumToInstallationRequested(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    hardwareId = "hardwareId",
                                ),
                            )

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumAddedToInstallation(
                                    aggregateId =
                                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                    "hardwareId",
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.ADD_MEDIUM_TO_INSTALLATION_REQUESTED,
                                encodeEvent(apiEvent),
                            )
                            .await()

                        client
                            .publishAsync(
                                Topics.Event.MEDIUM_ADDED_TO_INSTALLATION,
                                encodeEvent(apiEvent2),
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.ADD_MEDIUM_TO_INSTALLATION_REQUESTED,
                                Topics.Event.MEDIUM_ADDED_TO_INSTALLATION,
                            )
                        )
                        .await()

                    val requestToAddMediumToInstallationResult =
                        api.requestToAddMediumToInstallationAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            "hardwareId",
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                    val mediumAddedToInstallation =
                        requestToAddMediumToInstallationResult.mediumAddedToInstallationDeferred
                            .await()
                    mediumAddedToInstallation.aggregateId.shouldBe(
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")
                    )
                    val addMediumToInstallationRequested =
                        requestToAddMediumToInstallationResult
                            .addMediumToInstallationRequestedDeferred
                            .await()
                    addMediumToInstallationRequested.hardwareId.shouldBe("hardwareId")
                }
            }
        }
    })
