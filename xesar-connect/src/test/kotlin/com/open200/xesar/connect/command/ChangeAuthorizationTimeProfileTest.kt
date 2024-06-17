package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.changeAuthorizationTimeProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationTimeProfileChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.TimeProfileFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ChangeAuthorizationTimeProfileTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("change authorization time profile") {
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
                                Topics.Command.CHANGE_AUTHORIZATION_TIME_PROFILE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"name\":\"timeProfileName\",\"description\":null,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AuthorizationTimeProfileChanged(
                                    timeSeries = TimeProfileFixture.timeSeries,
                                    exceptionTimeSeries = TimeProfileFixture.exceptionTimeSerie,
                                    name = "timeProfileName",
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    validStandardTimeProfile = true))

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_TIME_PROFILE_CHANGED,
                                encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.AUTHORIZATION_TIME_PROFILE_CHANGED))
                        .await()

                    val result =
                        api.changeAuthorizationTimeProfileAsync(
                                timeProfileId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                timeProfileName = "timeProfileName",
                                timeSeries = TimeProfileFixture.timeSeries,
                                exceptionTimeSeries = TimeProfileFixture.exceptionTimeSerie,
                            )
                            .await()
                    result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    result.exceptionTimeSeries.shouldBeEqual(TimeProfileFixture.exceptionTimeSerie)
                    result.name.shouldBeEqual("timeProfileName")
                }
            }
        }
    })
