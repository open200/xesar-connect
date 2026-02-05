package com.open200.xesar.connect.it.event

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MobileRegistrationStateUpdated
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class EventMobileRegistrationStateUpdatedTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("should receive MobileRegistrationStateUpdated event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived = CompletableDeferred<ApiEvent<MobileRegistrationStateUpdated>>()

                val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                val commandId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.MOBILE_REGISTRATION_STATE_UPDATED))
                        .await()

                    api.onEvent<MobileRegistrationStateUpdated>(
                        TopicFilter(Topics.Event.MOBILE_REGISTRATION_STATE_UPDATED)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()

                    result.event.id.shouldBeEqual(smartphoneId)
                    result.event.registrationState!!.shouldBeEqual("REGISTERED")
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                MobileRegistrationStateUpdated(
                                    id = smartphoneId,
                                    registrationState = "REGISTERED",
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.MOBILE_REGISTRATION_STATE_UPDATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }
    })
