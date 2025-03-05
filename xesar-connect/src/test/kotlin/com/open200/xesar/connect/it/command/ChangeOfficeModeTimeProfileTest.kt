package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.changeOfficeModeTimeProfileAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.OfficeModeTimeProfileChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ChangeOfficeModeTimeProfileTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("change office mode time profile") {
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
                                Topics.Command.CHANGE_OFFICE_MODE_TIME_PROFILE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        println(commandContent)
                        commandContent.shouldBe(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"name\":\"name\",\"description\":null,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[],\"exceptionTimePointSeries\":[],\"timePointSeries\":[],\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                OfficeModeTimeProfileChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    name = "name",
                                    timeSeries = TimeProfileFixture.timeSeries,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.OFFICE_MODE_TIME_PROFILE_CHANGED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.OFFICE_MODE_TIME_PROFILE_CHANGED))
                        .await()
                    val result =
                        api.changeOfficeModeTimeProfileAsync(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                "name",
                                timeSeries = TimeProfileFixture.timeSeries,
                            )
                            .await()
                    result.id.shouldBe(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    result.timeSeries.shouldBe(TimeProfileFixture.timeSeries)
                }
            }
        }
    })
