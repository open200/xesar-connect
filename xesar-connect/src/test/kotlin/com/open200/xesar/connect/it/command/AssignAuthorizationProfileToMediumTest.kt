package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.assignAuthorizationProfileToMediumAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.*
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class AssignAuthorizationProfileToMediumTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("assign authorization profile to medium returning both events") {
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
                                Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumAuthorizationProfileChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                                ),
                            )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    accessBeginAt =
                                        LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                    changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991"),
                                ),
                            )

                        client
                            .publishAsync(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
                            .await()

                        client
                            .publishAsync(
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
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
                                Topics.Event.MEDIUM_CHANGED,
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                            )
                        )
                        .await()

                    val assignAuthorizationProfileToMediumResult =
                        api.assignAuthorizationProfileToMediumAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")
                        )
                    val test =
                        assignAuthorizationProfileToMediumResult
                            .mediumAuthorizationProfileChangedDeferred
                            .await()
                    test.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val mediumChanged =
                        assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                    mediumChanged.id.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                    )
                }
            }
        }
    })
