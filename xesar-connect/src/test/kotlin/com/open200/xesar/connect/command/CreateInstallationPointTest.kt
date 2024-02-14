package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.exception.OptionalEventException
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.exception.RequiredEventException
import com.open200.xesar.connect.extension.createInstallationPointAsync
import com.open200.xesar.connect.messages.ApiError
import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.encodeError
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.ComponentStatus
import com.open200.xesar.connect.testutils.MosquittoContainer
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class CreateInstallationPointTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())
        test("create installation point returning no event") {
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
                                Topics.Command.CREATE_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"componentType\":\"Cylinder\",\"aggregateId\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val createInstallationPointResult =
                        api.createInstallationPointAsync(
                            ComponentType.Cylinder,
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"))
                    shouldThrow<OptionalEventException> {
                        createInstallationPointResult.evvaComponentAddedDeferred.await()
                    }
                    shouldThrow<RequiredEventException> {
                        createInstallationPointResult.installationPointCreatedDeferred.await()
                    }
                }
            }
        }

        test("create installation point returning an api error") {
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
                                Topics.Command.CREATE_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBe(
                            "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")
                        val apiError =
                            ApiError(
                                "reason",
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                123)

                        client
                            .publishAsync(
                                Topics.Event.error(config.apiProperties.userId),
                                encodeError(apiError))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                Topics.Event.EVVA_COMPONENT_ADDED,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()

                    val createInstallationPointResult =
                        api.createInstallationPointAsync(
                            ComponentType.Cylinder,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                    shouldThrow<OptionalEventException> {
                        createInstallationPointResult.evvaComponentAddedDeferred.await()
                    }
                    shouldThrow<RequiredEventException> {
                        createInstallationPointResult.installationPointCreatedDeferred.await()
                    }

                    val apiError = createInstallationPointResult.apiErrorDeferred.await()
                    apiError.get().reason.shouldBe("reason")
                }
            }
        }

        test("create installation point returning the required event and an api error") {
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
                                Topics.Command.CREATE_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointCreated(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))
                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CREATED, encodeEvent(apiEvent))
                            .await()

                        val apiError =
                            ApiError(
                                "reason",
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                123)

                        client
                            .publishAsync(
                                Topics.Event.error(config.apiProperties.userId),
                                encodeError(apiError))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                Topics.Event.EVVA_COMPONENT_ADDED,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()

                    val createInstallationPointResult =
                        api.createInstallationPointAsync(
                            ComponentType.Cylinder,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val installationPointCreated =
                        createInstallationPointResult.installationPointCreatedDeferred.await()
                    installationPointCreated.id.shouldBe(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val apiError = createInstallationPointResult.apiErrorDeferred.await()
                    apiError.get().reason.shouldBe("reason")
                    shouldThrow<OptionalEventException> {
                        createInstallationPointResult.evvaComponentAddedDeferred.await()
                    }
                }
            }
        }

        test(
            "create installation point returning the required event and an error event but switch order of events") {
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
                                    Topics.Command.CREATE_INSTALLATION_POINT -> {
                                        commandReceived.complete(payload.decodeToString())
                                    }
                                }
                            }

                            simulatedBackendReady.complete(Unit)

                            val commandContent = commandReceived.await()
                            commandContent.shouldBeEqual(
                                "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                            val apiError =
                                ApiError(
                                    "reason",
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    123)

                            client
                                .publishAsync(
                                    Topics.Event.error(config.apiProperties.userId),
                                    encodeError(apiError))
                                .await()

                            val apiEvent =
                                ApiEvent(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    InstallationPointCreated(
                                        id =
                                            UUID.fromString(
                                                "43edc7cf-80ab-4486-86db-41cda2c7a2cd")))
                            client
                                .publishAsync(
                                    Topics.Event.INSTALLATION_POINT_CREATED, encodeEvent(apiEvent))
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        val api = XesarConnect.connectAndLoginAsync(config).await()
                        api.subscribeAsync(
                                Topics(
                                    Topics.Event.INSTALLATION_POINT_CREATED,
                                    Topics.Event.EVVA_COMPONENT_ADDED,
                                    Topics.Event.error(config.apiProperties.userId)))
                            .await()

                        val createInstallationPointResult =
                            api.createInstallationPointAsync(
                                ComponentType.Cylinder,
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                        val installationPointCreated =
                            createInstallationPointResult.installationPointCreatedDeferred.await()
                        installationPointCreated.id.shouldBe(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                        shouldThrow<OptionalEventException> {
                            createInstallationPointResult.await()
                        }
                    }
                }
            }

        test("add evva component with wrong publish back to get an Exception") {
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
                                Topics.Command.CREATE_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                EvvaComponentAdded(
                                    id = UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                    evvaComponentId =
                                        UUID.fromString("3a33c05b-133d-4b9d-a496-5d30dfd2d2c3"),
                                    type = ComponentType.Cylinder,
                                    stateChangedAt = LocalDateTime.MIN,
                                    status = ComponentStatus.AssembledPrepared))

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                "\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\"" /*encodeEvent(apiEvent)*/)
                            .await()
                        logger.info { "sent installation point changed" }

                        client
                            .publishAsync(Topics.Event.EVVA_COMPONENT_ADDED, encodeEvent(apiEvent2))
                            .await()

                        logger.info { "sent evva component added" }
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val createInstallationPointResult =
                        api.createInstallationPointAsync(
                            ComponentType.Cylinder,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                    shouldThrow<ParsingException> {
                        createInstallationPointResult.installationPointCreatedDeferred.await()
                    }
                    val evvaComponentAdded =
                        createInstallationPointResult.evvaComponentAddedDeferred.await()
                    evvaComponentAdded.type.shouldBe(ComponentType.Cylinder)
                }
            }
        }

        test("add evva component returning both events") {
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
                                Topics.Command.CREATE_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointCreated(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                EvvaComponentAdded(
                                    id = UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                    evvaComponentId =
                                        UUID.fromString("3a33c05b-133d-4b9d-a496-5d30dfd2d2c3"),
                                    type = ComponentType.Cylinder,
                                    stateChangedAt = LocalDateTime.MIN,
                                    status = ComponentStatus.AssembledPrepared))

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CREATED, encodeEvent(apiEvent))
                            .await()

                        client
                            .publishAsync(Topics.Event.EVVA_COMPONENT_ADDED, encodeEvent(apiEvent2))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val createInstallationPointResult =
                        api.createInstallationPointAsync(
                            ComponentType.Cylinder,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val test =
                        createInstallationPointResult.installationPointCreatedDeferred.await()
                    test.id.shouldBe(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val evvaComponentAdded =
                        createInstallationPointResult.evvaComponentAddedDeferred.await()
                    evvaComponentAdded.type.shouldBe(ComponentType.Cylinder)
                }
            }
        }

        test("add evva component but only required field will be returned") {
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
                                Topics.Command.CREATE_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointCreated(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CREATED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CREATED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val createInstallationPointResult =
                        api.createInstallationPointAsync(
                            ComponentType.Cylinder,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                    val installationPointCreated =
                        createInstallationPointResult.installationPointCreatedDeferred.await()
                    installationPointCreated.id.shouldBe(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                    shouldThrow<OptionalEventException> {
                        createInstallationPointResult.evvaComponentAddedDeferred.await()
                    }
                }
            }
        }
    })
