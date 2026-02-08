package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.changeZoneMetadataValueAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.ZoneChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ChangeZoneMetadataValueTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("change zone metadata value") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))

            val zoneId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.CHANGE_ZONE_METADATA_VALUE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"11111111-2222-3333-4444-555555555555\",\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Top Secret\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                ZoneChanged(
                                    name = "Test Zone",
                                    description = "Test Description",
                                    id = zoneId,
                                    entityMetadata =
                                        listOf(
                                            EntityMetadata(
                                                id = metadataId,
                                                name = "security clearance required",
                                                value = "Top Secret",
                                            )
                                        ),
                                ),
                            )

                        client
                            .publishAsync(Topics.Event.ZONE_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.ZONE_CHANGED)).await()

                    val result =
                        api.changeZoneMetadataValueAsync(
                                id = zoneId,
                                metadataId = metadataId,
                                value = "Top Secret",
                            )
                            .await()

                    result.id.shouldBeEqual(zoneId)
                    result.entityMetadata!!
                        .single { it.id == metadataId }
                        .value
                        ?.shouldBeEqual("Top Secret")
                }
            }
        }
    })
