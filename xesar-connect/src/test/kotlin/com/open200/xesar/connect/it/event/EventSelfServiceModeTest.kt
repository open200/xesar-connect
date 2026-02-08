package com.open200.xesar.connect.it.event

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import java.time.OffsetDateTime
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class EventSelfServiceModeTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("should receive SmartphoneLocked event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived = CompletableDeferred<ApiEvent<SmartphoneLocked>>()

                val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                val commandId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.SMARTPHONE_LOCKED)).await()

                    api.onEvent<SmartphoneLocked>(TopicFilter(Topics.Event.SMARTPHONE_LOCKED)) {
                        event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.aggregateId.shouldBeEqual(smartphoneId)
                    result.event.mediumIdentifier.shouldBeEqual(123L)
                    result.event.hasMasterKeyAccess.shouldBeEqual(true)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                SmartphoneLocked(
                                    aggregateId = smartphoneId,
                                    mediumIdentifier = 123L,
                                    hasMasterKeyAccess = true,
                                ),
                            )

                        client
                            .publishAsync(Topics.Event.SMARTPHONE_LOCKED, encodeEvent(apiEvent))
                            .await()
                    }
                }
            }
        }

        test("should receive SmartphoneRevokePending event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived = CompletableDeferred<ApiEvent<SmartphoneRevokePending>>()

                val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                val transactionId = UUID.fromString("1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a")
                val commandId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.SMARTPHONE_REVOKE_PENDING)).await()

                    api.onEvent<SmartphoneRevokePending>(
                        TopicFilter(Topics.Event.SMARTPHONE_REVOKE_PENDING)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.mediumId.shouldBeEqual(smartphoneId)
                    result.event.transactionId.shouldBeEqual(transactionId)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                SmartphoneRevokePending(
                                    mediumId = smartphoneId,
                                    transactionId = transactionId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.SMARTPHONE_REVOKE_PENDING,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }

        test("should receive SmartphoneUpdatePending event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived = CompletableDeferred<ApiEvent<SmartphoneUpdatePending>>()

                val xsMediumId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                val transactionId = UUID.fromString("1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a")
                val commandId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
                val ts = OffsetDateTime.parse("2023-06-01T10:00:00+02:00")
                val validFrom = OffsetDateTime.parse("2023-06-01T00:00:00+02:00")
                val validUntil = OffsetDateTime.parse("2024-06-01T00:00:00+02:00")

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.SMARTPHONE_UPDATE_PENDING)).await()

                    api.onEvent<SmartphoneUpdatePending>(
                        TopicFilter(Topics.Event.SMARTPHONE_UPDATE_PENDING)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.masterKey.shouldBeEqual(false)
                    result.event.xsMediumId.shouldBeEqual(xsMediumId)
                    result.event.transactionId.shouldBeEqual(transactionId)
                    result.event.version.shouldBeEqual(1)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                SmartphoneUpdatePending(
                                    masterKey = false,
                                    mediumDataFrame = "AABBCCDD",
                                    metadata = SmartphoneUpdatePending.Metadata(),
                                    officeMode = true,
                                    transactionId = transactionId,
                                    ts = ts,
                                    validFrom = validFrom,
                                    validUntil = validUntil,
                                    version = 1,
                                    xsId = "abc123hash",
                                    xsMediumId = xsMediumId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.SMARTPHONE_UPDATE_PENDING,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }
    })
