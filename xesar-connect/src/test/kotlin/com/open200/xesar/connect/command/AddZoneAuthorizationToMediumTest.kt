package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.addZoneAuthorizationToMedium
import com.open200.xesar.connect.messages.command.AuthorizationData
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.IndividualAuthorizationsAddedToMedium
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class AddZoneAuthorizationToMediumTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("add installation point authorization to medium") {
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
                                Topics.Command.ADD_ZONE_AUTHORIZATION_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"authorization\":{\"authorizationName\":\"authorizationName\",\"authorizationProfileId\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"timeProfileName\":\"timeProfileName\",\"authorizationId\":\"8c124504-8263-4201-8b17-f49a6c2f8671\",\"installationPoint\":false,\"timeProfileId\":\"e9b31e62-8969-4794-a219-8c81ff10c91d\"},\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")
                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                IndividualAuthorizationsAddedToMedium(
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        client
                            .publishAsync(
                                Topics.Event.INDIVIDUAL_AUTHORIZATIONS_ADDED_TO_MEDIUM,
                                encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    XesarConnectTestHelper.connect(config).use { api ->
                        api.subscribeAsync(
                                Topics(Topics.Event.INDIVIDUAL_AUTHORIZATIONS_ADDED_TO_MEDIUM))
                            .await()
                        val authorizationData =
                            AuthorizationData(
                                "authorizationName",
                                UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                "timeProfileName",
                                UUID.fromString("8c124504-8263-4201-8b17-f49a6c2f8671"),
                                false,
                                UUID.fromString("e9b31e62-8969-4794-a219-8c81ff10c91d"))
                        val result =
                            api.addZoneAuthorizationToMedium(
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    authorizationData)
                                .await()
                        result.id.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    }
                }
            }
        }
    })
