package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.createZone
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.ZoneCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.testutils.MosquittoContainer
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class CreateZoneTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("create zone") {
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
                                Topics.Command.CREATE_ZONE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPoints\":[\"720eb694-a085-47e8-8f18-512aa1a63bef\",\"c457a028-dd08-4040-a1bb-17767e2b2c28\"],\"name\":\"zoneName\",\"description\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                ZoneCreated(
                                    123,
                                    UUID.fromString("dc6f8102-af8f-4e8d-afd4-2d6ed1f5722d"),
                                    "zoneName",
                                    null,
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        client
                            .publishAsync(Topics.Event.ZONE_CREATED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnect.connectAndLoginAsync(config).await().use { api ->
                        api.subscribeAsync(Topics(Topics.Event.ZONE_CREATED)).await()
                        val result =
                            api.createZone(
                                    name = "zoneName",
                                    zoneId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    installationPoints =
                                        listOf(
                                            UUID.fromString("720eb694-a085-47e8-8f18-512aa1a63bef"),
                                            UUID.fromString(
                                                "c457a028-dd08-4040-a1bb-17767e2b2c28")))
                                .await()
                        result.name.shouldBeEqual("zoneName")
                        result.id.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    }
                }
            }
        }
    })
