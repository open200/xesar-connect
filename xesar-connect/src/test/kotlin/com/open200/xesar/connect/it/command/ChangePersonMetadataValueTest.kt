package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.changePersonMetadataValueAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ChangePersonMetadataValueTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("change person metadata value via externalId") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))

            val externalId = "P-12345"
            val personId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.CHANGE_PERSON_METADATA_VALUE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        // Order-sensitive raw JSON
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"externalId\":\"P-12345\",\"id\":null,\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Sales\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        // Simulate success PERSON_CHANGED with updated entity metadata
                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                PersonChanged(
                                    firstName = "Tester",
                                    lastName = "Testington",
                                    id = personId,
                                    entityMetadata =
                                        listOf(
                                            EntityMetadata(
                                                id = metadataId,
                                                name = "department",
                                                value = "Sales",
                                            )
                                        ),
                                ),
                            )

                        client
                            .publishAsync(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.PERSON_CHANGED)).await()

                    val result =
                        api.changePersonMetadataValueAsync(
                                externalId = externalId,
                                id = null,
                                metadataId = metadataId,
                                value = "Sales",
                            )
                            .await()

                    result.id.shouldBeEqual(personId)
                    result.entityMetadata!!
                        .single { it.id == metadataId }
                        .value
                        ?.shouldBeEqual("Sales")
                }
            }
        }

        test("change person metadata value via id") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))

            val personId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000002")

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.CHANGE_PERSON_METADATA_VALUE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        // Order-sensitive raw JSON
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"externalId\":null,\"id\":\"$personId\",\"metadataId\":\"$metadataId\",\"value\":\"Team Blue\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                PersonChanged(
                                    firstName = "Tester",
                                    lastName = "Testington",
                                    id = personId,
                                    entityMetadata =
                                        listOf(
                                            EntityMetadata(
                                                id = metadataId,
                                                name = "team",
                                                value = "Team Blue",
                                            )
                                        ),
                                ),
                            )

                        client
                            .publishAsync(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.PERSON_CHANGED)).await()

                    val result =
                        api.changePersonMetadataValueAsync(
                                id = personId,
                                metadataId = metadataId,
                                value = "Team Blue",
                            )
                            .await()

                    result.id.shouldBeEqual(personId)
                    result.entityMetadata!!
                        .single { it.id == metadataId }
                        .value
                        ?.shouldBeEqual("Team Blue")
                }
            }
        }
    })
