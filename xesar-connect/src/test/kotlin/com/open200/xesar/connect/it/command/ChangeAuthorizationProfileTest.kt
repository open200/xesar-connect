package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.changeAuthorizationProfileAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.*
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ChangeAuthorizationProfileTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("change authorization profile returning all events") {
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
                                Topics.Command.CHANGE_AUTHORIZATION_PROFILE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"installationPoints\":[],\"manualOfficeMode\":true,\"name\":\"new name\",\"description\":\"new description\",\"standardTimeProfile\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"zones\":[],\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AuthorizationProfileChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AuthorizationProfileAccessChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))
                        val apiEvent3 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                AuthorizationProfileInfoChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_PROFILE_CHANGED, encodeEvent(apiEvent))
                            .await()

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_PROFILE_ACCESS_CHANGED,
                                encodeEvent(apiEvent2))
                            .await()

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_PROFILE_INFO_CHANGED,
                                encodeEvent(apiEvent3))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.AUTHORIZATION_PROFILE_CHANGED,
                                Topics.Event.AUTHORIZATION_PROFILE_ACCESS_CHANGED,
                                Topics.Event.AUTHORIZATION_PROFILE_INFO_CHANGED))
                        .await()

                    val changeAuthorizationProfileResult =
                        api.changeAuthorizationProfileAsync(
                            emptyList(),
                            true,
                            "new name",
                            "new description",
                            null,
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            emptyList())
                    val authorizationProfileInfoChanged =
                        changeAuthorizationProfileResult.authorizationProfileInfoChangedDeferred
                            .await()

                    authorizationProfileInfoChanged.id.shouldBe(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val authorizationProfileChanged =
                        changeAuthorizationProfileResult.authorizationProfileChangedDeferred.await()
                    authorizationProfileChanged.id.shouldBe(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                    val authorizationProfileAccessChanged =
                        changeAuthorizationProfileResult.authorizationProfileAccessChangedDeferred
                            .await()
                    authorizationProfileAccessChanged.id.shouldBe(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                }
            }
        }
    })
