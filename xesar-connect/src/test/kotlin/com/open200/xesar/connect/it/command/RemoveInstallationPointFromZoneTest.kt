package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.removeInstallationPointFromZoneAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointsInZoneChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class RemoveInstallationPointFromZoneTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("remove installation point from zone") {
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
                                Topics.Command.REMOVE_INSTALLATION_POINT_FROM_ZONE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"id\":\"8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointsInZoneChanged(
                                    accessId = 123,
                                    removedInstallationPoints =
                                        listOf(
                                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                                        ),
                                    aggregateId =
                                        UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f"),
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED))
                        .await()
                    val result =
                        api.removeInstallationPointFromZoneAsync(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f"),
                            )
                            .await()
                    result.aggregateId.shouldBeEqual(
                        UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f")
                    )
                    result.removedInstallationPoints.shouldBeEqual(
                        listOf(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    )
                }
            }
        }
    })
