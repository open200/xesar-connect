package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.deleteEntityMetadataDefinitionAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.EntityType
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class DeleteEntityMetadataDefinitionTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("delete entity metadata definition") {
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
                                Topics.Command.DELETE_ENTITY_METADATA_DEFINITION -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"entityType\":\"PERSON\",\"names\":[\"department\"],\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                PartitionChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    entityMetadataDefinitions =
                                        PartitionChanged.EntityMetadataDefinitions(
                                            authorizationProfiles = emptyList(),
                                            identificationMedia = emptyList(),
                                            installationPoints = emptyList(),
                                            persons = listOf(
                                                EntityMetadata(
                                                    id = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001"),
                                                    name = "nickname"
                                                )
                                            ),
                                            zones = emptyList(),
                                        ),
                                ),
                            )

                        client.publishAsync(
                            Topics.Event.PARTITION_CHANGED,
                            encodeEvent(apiEvent)
                        ).await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.PARTITION_CHANGED)).await()

                    val result = api.deleteEntityMetadataDefinitionAsync(
                        EntityType.PERSON,
                        "department",
                    ).await()

                    result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    result.entityMetadataDefinitions!!.persons.map { it.name }
                        .shouldBeEqual(listOf("nickname"))
                }
            }
        }
    })
