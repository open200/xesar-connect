package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.changeAuthorizationProfileMetadataValueAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationProfileInfoChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ChangeAuthorizationProfileMetadataValueTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("change authorization profile metadata value") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))

            val authorizationProfileId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.CHANGE_AUTHORIZATION_PROFILE_METADATA_VALUE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"11111111-2222-3333-4444-555555555555\",\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Test Value\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AuthorizationProfileInfoChanged(
                                    id = authorizationProfileId,
                                    entityMetadata =
                                        listOf(
                                            EntityMetadata(
                                                id = metadataId,
                                                name = "test",
                                                value = "Test Value",
                                            )
                                        ),
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_PROFILE_INFO_CHANGED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.AUTHORIZATION_PROFILE_INFO_CHANGED))
                        .await()

                    val result =
                        api.changeAuthorizationProfileMetadataValueAsync(
                                id = authorizationProfileId,
                                metadataId = metadataId,
                                value = "Test Value",
                            )
                            .await()

                    result.id?.shouldBeEqual(authorizationProfileId)
                    result.entityMetadata!!
                        .single { it.id == metadataId }
                        .value
                        ?.shouldBeEqual("Test Value")
                }
            }
        }
    })
