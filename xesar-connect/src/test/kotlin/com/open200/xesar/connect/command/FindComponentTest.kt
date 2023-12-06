package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.exception.HttpErrorException
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.testutils.InstallationPointFixture
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
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
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"enable\":true,\"token\":\"${XesarConnectTestHelper.TOKEN}\"}")

                        val errorEvent =
                            ApiEvent(
                                UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                                ErrorEvent(
                                    correlationId =
                                        UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    error = HttpStatusCode.InternalServerError.value))

                        client
                            .publishAsync(
                                Topics.Event.error(config.apiProperties.userId),
                                encodeEvent(errorEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
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
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"enable\":true,\"token\":\"${XesarConnectTestHelper.TOKEN}\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                FindComponentPerformed("ok"))
                        val findComponentPerformed = encodeEvent(apiEvent)

                        client
                            .publishAsync(
                                Topics.Event.FIND_COMPONENT_PERFORMED, findComponentPerformed)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
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
