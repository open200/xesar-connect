package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.createAuthorizationTimeProfileAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationTimeProfileCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.messages.query.TimeProfileType
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class CreateAuthorizationTimeProfileTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("create authorization time profile") {
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
                                Topics.Command.CREATE_AUTHORIZATION_TIME_PROFILE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[],\"name\":\"timeProfileName\",\"description\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AuthorizationTimeProfileCreated(
                                    timeSeries = TimeProfileFixture.timeSeries,
                                    name = "timeProfileName",
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    type = TimeProfileType.AUTHORIZATION_PROFILE,
                                    validStandardTimeProfile = true,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_TIME_PROFILE_CREATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.AUTHORIZATION_TIME_PROFILE_CREATED))
                        .await()
                    val result =
                        api.createAuthorizationTimeProfileAsync(
                                name = "timeProfileName",
                                timeProfileId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                timeSeries = TimeProfileFixture.timeSeries,
                            )
                            .await()
                    result.name.shouldBeEqual("timeProfileName")
                    result.type.shouldBeEqual(TimeProfileType.AUTHORIZATION_PROFILE)
                }
            }
        }
    })
