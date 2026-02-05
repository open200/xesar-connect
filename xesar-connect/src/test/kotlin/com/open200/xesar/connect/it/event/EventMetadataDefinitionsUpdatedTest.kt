package com.open200.xesar.connect.it.event

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import java.util.UUID
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class EventMetadataDefinitionsUpdatedTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        val entityId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
        val metadataId = UUID.fromString("a4b319e0-1281-40ae-89d7-5c541d77a757")
        val commandId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")

        val entityMetadata =
            listOf(EntityMetadata(id = metadataId, name = "testMetadata", value = "testValue"))

        test("should receive AuthorizationProfileMetadataDefinitionsUpdated event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived =
                    CompletableDeferred<ApiEvent<AuthorizationProfileMetadataDefinitionsUpdated>>()

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(Topics.Event.AUTHORIZATION_PROFILE_METADATA_DEFINITIONS_UPDATED)
                        )
                        .await()

                    api.onEvent<AuthorizationProfileMetadataDefinitionsUpdated>(
                        TopicFilter(Topics.Event.AUTHORIZATION_PROFILE_METADATA_DEFINITIONS_UPDATED)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.id.shouldBeEqual(entityId)
                    result.event.entityMetadata.shouldBeEqual(entityMetadata)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                AuthorizationProfileMetadataDefinitionsUpdated(
                                    entityMetadata = entityMetadata,
                                    id = entityId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.AUTHORIZATION_PROFILE_METADATA_DEFINITIONS_UPDATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }

        test("should receive InstallationPointMetadataDefinitionsUpdated event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived =
                    CompletableDeferred<ApiEvent<InstallationPointMetadataDefinitionsUpdated>>()

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(Topics.Event.INSTALLATION_POINT_METADATA_DEFINITIONS_UPDATED)
                        )
                        .await()

                    api.onEvent<InstallationPointMetadataDefinitionsUpdated>(
                        TopicFilter(Topics.Event.INSTALLATION_POINT_METADATA_DEFINITIONS_UPDATED)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.id.shouldBeEqual(entityId)
                    result.event.entityMetadata.shouldBeEqual(entityMetadata)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                InstallationPointMetadataDefinitionsUpdated(
                                    entityMetadata = entityMetadata,
                                    id = entityId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_METADATA_DEFINITIONS_UPDATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }

        test("should receive MediumMetadataDefinitionsUpdated event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived =
                    CompletableDeferred<ApiEvent<MediumMetadataDefinitionsUpdated>>()

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.MEDIUM_METADATA_DEFINITIONS_UPDATED))
                        .await()

                    api.onEvent<MediumMetadataDefinitionsUpdated>(
                        TopicFilter(Topics.Event.MEDIUM_METADATA_DEFINITIONS_UPDATED)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.id.shouldBeEqual(entityId)
                    result.event.entityMetadata.shouldBeEqual(entityMetadata)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                MediumMetadataDefinitionsUpdated(
                                    entityMetadata = entityMetadata,
                                    id = entityId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.MEDIUM_METADATA_DEFINITIONS_UPDATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }

        test("should receive PersonMetadataDefinitionsUpdated event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived =
                    CompletableDeferred<ApiEvent<PersonMetadataDefinitionsUpdated>>()

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.PERSON_METADATA_DEFINITIONS_UPDATED))
                        .await()

                    api.onEvent<PersonMetadataDefinitionsUpdated>(
                        TopicFilter(Topics.Event.PERSON_METADATA_DEFINITIONS_UPDATED)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.id.shouldBeEqual(entityId)
                    result.event.entityMetadata.shouldBeEqual(entityMetadata)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                PersonMetadataDefinitionsUpdated(
                                    entityMetadata = entityMetadata,
                                    id = entityId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.PERSON_METADATA_DEFINITIONS_UPDATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }

        test("should receive ZoneMetadataDefinitionsUpdated event") {
            withTimeout(5000) {
                val clientReady = CompletableDeferred<Unit>()
                val eventReceived = CompletableDeferred<ApiEvent<ZoneMetadataDefinitionsUpdated>>()

                launch {
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.ZONE_METADATA_DEFINITIONS_UPDATED))
                        .await()

                    api.onEvent<ZoneMetadataDefinitionsUpdated>(
                        TopicFilter(Topics.Event.ZONE_METADATA_DEFINITIONS_UPDATED)
                    ) { event ->
                        eventReceived.complete(event)
                    }

                    clientReady.complete(Unit)

                    val result = eventReceived.await()
                    result.event.id.shouldBeEqual(entityId)
                    result.event.entityMetadata.shouldBeEqual(entityMetadata)
                }

                launch {
                    clientReady.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val apiEvent =
                            ApiEvent(
                                commandId,
                                ZoneMetadataDefinitionsUpdated(
                                    entityMetadata = entityMetadata,
                                    id = entityId,
                                ),
                            )

                        client
                            .publishAsync(
                                Topics.Event.ZONE_METADATA_DEFINITIONS_UPDATED,
                                encodeEvent(apiEvent),
                            )
                            .await()
                    }
                }
            }
        }
    })
