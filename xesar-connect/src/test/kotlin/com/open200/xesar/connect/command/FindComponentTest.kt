package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.exception.HttpErrorException
import com.open200.xesar.connect.extension.findComponent
import com.open200.xesar.connect.messages.ApiError
import com.open200.xesar.connect.messages.encodeError
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.FindComponentPerformed
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.testutils.InstallationPointFixture
import com.open200.xesar.connect.testutils.MosquittoContainer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.http.*
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class FindComponentTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("findComponent with HttpErrorException") {
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
                                Topics.Command.FIND_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"enable\":true,\"token\":\"${MosquittoContainer.TOKEN}\"}")

                        val apiError =
                            ApiError(
                                correlationId =
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                error = HttpStatusCode.InternalServerError.value)

                        client
                            .publishAsync(
                                Topics.Event.error(config.apiProperties.userId),
                                encodeError(apiError))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnect.connectAndLoginAsync(config).await().use { api ->
                        shouldThrow<HttpErrorException> {
                            api.findComponent(
                                    InstallationPointFixture.installationPointFixture.id, true)
                                .await()
                        }
                    }
                }
            }
        }
        test("find Component with success") {
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
                                Topics.Command.FIND_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"enable\":true,\"token\":\"${MosquittoContainer.TOKEN}\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                FindComponentPerformed("ok"))

                        client
                            .publishAsync(
                                Topics.Event.FIND_COMPONENT_PERFORMED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnect.connectAndLoginAsync(config).await().use { api ->
                        api.subscribeAsync(Topics(Topics.Event.FIND_COMPONENT_PERFORMED)).await()
                        val result =
                            api.findComponent(
                                    InstallationPointFixture.installationPointFixture.id, true)
                                .await()

                        result.ok.shouldBeEqual("ok")
                    }
                }
            }
        }
    })
