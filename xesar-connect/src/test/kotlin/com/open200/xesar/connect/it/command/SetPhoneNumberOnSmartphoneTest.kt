package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.setPhoneNumberOnSmartphoneAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MediumChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class SetPhoneNumberOnSmartphoneTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("set phone number on smartphone media") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))

            val smartphoneMediaId = UUID.fromString("7b21f75f-3fa6-41c8-acff-a14839331b25")
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.SET_PHONE_NUMBER_ON_SMARTPHONE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"7b21f75f-3fa6-41c8-acff-a14839331b25\",\"phoneNumber\":\"+0123456789\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumChanged(
                                    accessBeginAt = LocalDateTime.parse("2024-08-04T10:05"),
                                    disengagePeriod = DisengagePeriod.SHORT,
                                    authorizationProfileId =
                                        UUID.fromString("00000000-0000-0000-0000-000000000001"),
                                    validityDuration = null,
                                    validUntil = null,
                                    changedAt =
                                        LocalDateTime.parse("2024-08-08T08:00:40.671784826"),
                                    id = UUID.fromString("16ea9a30-bc2a-4b99-b3d8-73981f01e2d2"),
                                    label = "Test12348",
                                    validFrom = null,
                                    accessEndAt = LocalDateTime.parse("2079-12-31T23:50"),
                                    phoneNumber = "+0123456789"))

                        client
                            .publishAsync(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(Topics.Event.PHONE_NUMBER_CHANGED, Topics.Event.MEDIUM_CHANGED))
                        .await()
                    val result =
                        api.setPhoneNumberOnSmartphoneAsync("+0123456789", smartphoneMediaId)
                            .mediumChangedDeferred
                            .await()
                    result.phoneNumber!!.shouldBeEqual("+0123456789")
                }
            }
        }
    })
