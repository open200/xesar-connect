import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.exception.OptionalEventException
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.exception.RequiredEventException
import com.open200.xesar.connect.extension.addEvvaComponentAsync
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

class AddEvvaComponentTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())
        test("add evva component returning no event") {
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
                                Topics.Command.ADD_EVVA_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val addEvvaComponentEventPair =
                        api.addEvvaComponentAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            ComponentType.Cylinder)
                    shouldThrow<OptionalEventException> {
                        addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                    }
                    shouldThrow<RequiredEventException> {
                        addEvvaComponentEventPair.installationPointChangedDeferred.await()
                    }
                }
            }
        }

        test("add evva component returning an api error") {
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
                                Topics.Command.ADD_EVVA_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBe(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

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
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                Topics.Event.EVVA_COMPONENT_ADDED,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()

                    val addEvvaComponentEventPair =
                        api.addEvvaComponentAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ComponentType.Cylinder)

                    shouldThrow<OptionalEventException> {
                        addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                    }
                    shouldThrow<RequiredEventException> {
                        addEvvaComponentEventPair.installationPointChangedDeferred.await()
                    }

                    val apiError = addEvvaComponentEventPair.apiErrorDeferred.await()
                    apiError.get().reason.shouldBe("reason")
                }
            }
        }

        test("add evva component returning the required event and an error event") {
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
                                Topics.Command.ADD_EVVA_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))
                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
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
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                Topics.Event.EVVA_COMPONENT_ADDED,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()

                    val addEvvaComponentEventPair =
                        api.addEvvaComponentAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ComponentType.Cylinder)
                    val installationPointChanged =
                        addEvvaComponentEventPair.installationPointChangedDeferred.await()
                    installationPointChanged.aggregateId.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val apiError = addEvvaComponentEventPair.apiErrorDeferred.await()
                    apiError.get().reason.shouldBe("reason")
                    shouldThrow<OptionalEventException> {
                        addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                    }
                }
            }
        }

        test(
            "add evva component returning the required event and an error event but switch order of events") {
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
                                    Topics.Command.ADD_EVVA_COMPONENT -> {
                                        commandReceived.complete(payload.decodeToString())
                                    }
                                }
                            }

                            simulatedBackendReady.complete(Unit)

                            val commandContent = commandReceived.await()
                            commandContent.shouldBeEqual(
                                "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

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
                                    InstallationPointChanged(
                                        aggregateId =
                                            UUID.fromString(
                                                "43edc7cf-80ab-4486-86db-41cda2c7a2cd")))
                            client
                                .publishAsync(
                                    Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        val api = XesarConnect.connectAndLoginAsync(config).await()
                        api.subscribeAsync(
                                Topics(
                                    Topics.Event.INSTALLATION_POINT_CHANGED,
                                    Topics.Event.EVVA_COMPONENT_ADDED,
                                    Topics.Event.error(config.apiProperties.userId)))
                            .await()

                        val addEvvaComponentEventPair =
                            api.addEvvaComponentAsync(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                ComponentType.Cylinder)
                        val installationPointChanged =
                            addEvvaComponentEventPair.installationPointChangedDeferred.await()
                        installationPointChanged.aggregateId.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                        shouldThrow<OptionalEventException> { addEvvaComponentEventPair.await() }
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
                                Topics.Command.ADD_EVVA_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

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
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                "\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\"" /*encodeEvent(apiEvent)*/)
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
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val addEvvaComponentEventPair =
                        api.addEvvaComponentAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            ComponentType.Cylinder)

                    shouldThrow<ParsingException> {
                        addEvvaComponentEventPair.installationPointChangedDeferred.await()
                    }
                    val evvaComponentAdded =
                        addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
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
                                Topics.Command.ADD_EVVA_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

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
                                Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
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
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val addEvvaComponentEventPair =
                        api.addEvvaComponentAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            ComponentType.Cylinder)
                    val test = addEvvaComponentEventPair.installationPointChangedDeferred.await()
                    test.aggregateId.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val evvaComponentAdded =
                        addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
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
                                Topics.Command.ADD_EVVA_COMPONENT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.INSTALLATION_POINT_CHANGED,
                                Topics.Event.EVVA_COMPONENT_ADDED))
                        .await()

                    val addEvvaComponentEventPair =
                        api.addEvvaComponentAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            ComponentType.Cylinder)
                    val installationPointChanged =
                        addEvvaComponentEventPair.installationPointChangedDeferred.await()
                    installationPointChanged.aggregateId.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                    shouldThrow<OptionalEventException> {
                        addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                    }
                }
            }
        }
    })
