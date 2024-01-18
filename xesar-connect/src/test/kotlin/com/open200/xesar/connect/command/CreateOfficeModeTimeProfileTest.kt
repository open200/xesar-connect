package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.createOfficeModeTimeProfile
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.OfficeModeTimeProfileCreated
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

class CreateOfficeModeTimeProfileTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("create office mode time profile") {
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
                                Topics.Command.CREATE_OFFICE_MODE_TIME_PROFILE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"timeSeries\":[{\"times\":[{\"start\":\"14:15\",\"end\":\"14:15\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[],\"exceptionTimePointSeries\":[],\"name\":\"timeProfileName\",\"description\":null,\"timePointSeries\":[],\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                OfficeModeTimeProfileCreated(
                                    name = "timeProfileName",
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    timeSeries = TimeProfileFixture.timeSeries,
                                    exceptionTimeSeries = listOf(),
                                    exceptionTimePointSeries = listOf(),
                                    timePointSeries = listOf()))

                        client
                            .publishAsync(
                                Topics.Event.OFFICE_MODE_TIME_PROFILE_CREATED,
                                encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnect.connectAndLoginAsync(config).await().use { api ->
                        api.subscribeAsync(Topics(Topics.Event.OFFICE_MODE_TIME_PROFILE_CREATED))
                            .await()
                        val result =
                            api.createOfficeModeTimeProfile(
                                    name = "timeProfileName",
                                    timeSeries = TimeProfileFixture.timeSeries,
                                    timeProfileId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                                .await()
                        result.name.shouldBeEqual("timeProfileName")
                        result.timeSeries.shouldBeEqual(TimeProfileFixture.timeSeries)
                    }
                }
            }
        }
    })
