package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.confirmSmartphoneRevokeAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.SmartphoneRevokeConfirmed
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class ConfirmSmartphoneRevokeTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("confirm smartphone revoke in Self Service Mode") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))

            val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
            val transactionId = UUID.fromString("1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a")

            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command.CONFIRM_SMARTPHONE_REVOKE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                                "\"mediumId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                                "\"transactionId\":\"1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a\"," +
                                "\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                SmartphoneRevokeConfirmed(
                                    mediumId = smartphoneId,
                                    transactionId = transactionId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.SMARTPHONE_REVOKE_CONFIRMED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.SMARTPHONE_REVOKE_CONFIRMED)).await()

                    val result =
                        api.confirmSmartphoneRevokeAsync(smartphoneId, transactionId).await()

                    result.mediumId.shouldBeEqual(smartphoneId)
                    result.transactionId.shouldBeEqual(transactionId)
                }
            }
        }
    })
