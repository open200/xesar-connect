package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.setMobileServiceModeAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.MobileServiceMode
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class SetMobileServiceModeTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("set mobile service mode to SELF_SERVICE") {
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
                                Topics.Command.SET_MOBILE_SERVICE_MODE -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        // Enums are serialized using @SerialName -> "xms"
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"mobileServiceMode\":\"self-service\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                        )

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                PartitionChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    mobileServiceMode = MobileServiceMode.SELF_SERVICE,
                                ),
                            )

                        client
                            .publishAsync(Topics.Event.PARTITION_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }

                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.PARTITION_CHANGED)).await()

                    val result = api.setMobileServiceModeAsync(MobileServiceMode.SELF_SERVICE).await()

                    result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    result.mobileServiceMode?.shouldBeEqual(MobileServiceMode.SELF_SERVICE)
                }
            }
        }
    })

