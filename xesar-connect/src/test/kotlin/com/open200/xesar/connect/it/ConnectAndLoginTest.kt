package com.open200.xesar.connect.it

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.UserCredentials
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.exception.UnauthorizedLoginAttemptException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ConnectAndLoginTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("connect and login as well as logout with testcontainers work") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("a7d184b6-dd9d-4bb8-acb7-0b51ada3e3b7"))
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val loginReceived = CompletableDeferred<String>()
                val logoutReceived = CompletableDeferred<String>()
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.LOGIN -> {
                                    loginReceived.complete(payload.decodeToString())
                                }
                                Topics.Command.LOGOUT -> {
                                    logoutReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val loginContent = loginReceived.await()
                        loginContent.shouldBeEqual(
                            "{\"commandId\":\"a7d184b6-dd9d-4bb8-acb7-0b51ada3e3b7\",\"username\":\"fordprefect\",\"password\":\"foobar\"}"
                        )
                        client
                            .publishAsync(
                                Topics.Event.loggedIn(config.apiProperties.userId),
                                "{\"commandId\":\"a7d184b6-dd9d-4bb8-acb7-0b51ada3e3b7\",\"event\":{\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}}",
                            )
                            .await()

                        val logoutContent = logoutReceived.await()
                        logoutContent.shouldBeEqual(
                            "{\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        client
                            .publishAsync(
                                Topics.Event.LOGGED_OUT,
                                "{\"event\": {\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}}",
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val userCredentials = UserCredentials("fordprefect", "foobar")

                    val it = XesarConnect.connectAndLoginAsync(config, userCredentials).await()
                    it.getSubscribedTopics()
                        .shouldBe(
                            listOf(
                                Topics.Event.loggedIn(
                                    UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
                                ),
                                Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                                Topics.Event.LOGGED_OUT,
                                Topics.Event.error(config.apiProperties.userId),
                            )
                        )

                    it.shouldBeInstanceOf<XesarConnect>()
                    it.logoutAsync().await()
                }
            }
        }

        test(
            "connect and login with testcontainers should throw Exception when wrong username, password"
        ) {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("a7d184b6-dd9d-4bb8-acb7-0b51ada3e3b7"))
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val loginReceived = CompletableDeferred<String>()
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { _, payload ->
                            loginReceived.complete(payload.decodeToString())
                        }

                        simulatedBackendReady.complete(Unit)
                        val messageContent = loginReceived.await()
                        messageContent.shouldBeEqual(
                            "{\"commandId\":\"a7d184b6-dd9d-4bb8-acb7-0b51ada3e3b7\",\"username\":\"Unauthorized\",\"password\":\"foobar\"}"
                        )
                        client
                            .publishAsync(
                                Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                                "{\"event\":{\"username\":\"Unauthorized\", \"channel\":\"API\"}}",
                            )
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val wrongUserCredentials = UserCredentials("Unauthorized", "foobar")

                    shouldThrow<UnauthorizedLoginAttemptException> {
                        XesarConnect.connectAndLoginAsync(config, wrongUserCredentials).await()
                    }
                }
            }
        }
    })
