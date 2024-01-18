package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.addInstallationPointToZone
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointsInZoneChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.testutils.MosquittoContainer
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class AddInstallationPointToZoneTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("add installation point to zone") {
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
                                Topics.Command.ADD_INSTALLATION_POINT_TO_ZONE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBe(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")
                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointsInZoneChanged(
                                    123,
                                    listOf(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")),
                                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")))

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED,
                                encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnect.connectAndLoginAsync(config).await().use { api ->
                        api.subscribeAsync(Topics(Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED))
                            .await()
                        val result =
                            api.addInstallationPointToZone(
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"))
                                .await()
                        result.addedInstallationPoints.size.shouldBeEqual(1)
                        result.aggregateId.shouldBeEqual(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"))
                        result.accessId.shouldBeEqual(123)
                    }
                }
            }
        }
    })
