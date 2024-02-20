package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.exception.OptionalEventException
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.extension.assignAuthorizationProfileToMediumAsync
import com.open200.xesar.connect.messages.ApiError
import com.open200.xesar.connect.messages.encodeError
import com.open200.xesar.connect.messages.event.*
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

class AssignAuthorizationProfileToMediumTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())
        test("assign authorization profile to medium returning no event") {
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
                                Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)
                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.MEDIUM_CHANGED,
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED))
                        .await()

                    val assignAuthorizationProfileToMediumResult =
                        api.assignAuthorizationProfileToMediumAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        )
                    shouldThrow<OptionalEventException> {
                        assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                    }
                    shouldThrow<OptionalEventException> {
                        assignAuthorizationProfileToMediumResult
                            .mediumAuthorizationProfileChangedDeferred
                            .await()
                    }
                }
            }
        }

        test("assign authorization profile to medium returning an api error") {
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
                                Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBe(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

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
                                Topics.Event.MEDIUM_CHANGED,
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()

                    val assignAuthorizationProfileToMediumResult =
                        api.assignAuthorizationProfileToMediumAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )

                    shouldThrow<OptionalEventException> {
                        assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                    }
                    shouldThrow<OptionalEventException> {
                        assignAuthorizationProfileToMediumResult
                            .mediumAuthorizationProfileChangedDeferred
                            .await()
                    }

                    val apiError = assignAuthorizationProfileToMediumResult.apiErrorDeferred.await()
                    apiError.get().reason.shouldBe("reason")
                }
            }
        }

        test("assign authorization profile to medium returning one event and an error event") {
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
                                Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()
                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    accessBeginAt =
                                        LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                    changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991")))
                        client
                            .publishAsync(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
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
                                Topics.Event.MEDIUM_CHANGED,
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                                Topics.Event.error(config.apiProperties.userId)))
                        .await()

                    val assignAuthorizationProfileToMediumResult =
                        api.assignAuthorizationProfileToMediumAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                    val mediumChanged =
                        assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                    mediumChanged.id.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val apiError = assignAuthorizationProfileToMediumResult.apiErrorDeferred.await()
                    apiError.get().reason.shouldBe("reason")
                    shouldThrow<OptionalEventException> {
                        assignAuthorizationProfileToMediumResult
                            .mediumAuthorizationProfileChangedDeferred
                            .await()
                    }
                }
            }
        }

        test(
            "assign authorization profile to medium returning one event and an error event but switch order of events") {
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
                                    Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                        commandReceived.complete(payload.decodeToString())
                                    }
                                }
                            }

                            simulatedBackendReady.complete(Unit)

                            val commandContent = commandReceived.await()
                            commandContent.shouldBeEqual(
                                "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

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
                                    MediumAuthorizationProfileChanged(
                                        id =
                                            UUID.fromString(
                                                "43edc7cf-80ab-4486-86db-41cda2c7a2cd")))
                            client
                                .publishAsync(
                                    Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                                    encodeEvent(apiEvent))
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        val api = XesarConnect.connectAndLoginAsync(config).await()
                        api.subscribeAsync(
                                Topics(
                                    Topics.Event.MEDIUM_CHANGED,
                                    Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                                    Topics.Event.error(config.apiProperties.userId)))
                            .await()

                        val assignAuthorizationProfileToMediumResult =
                            api.assignAuthorizationProfileToMediumAsync(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            )
                        val MediumAuthorizationProfileChanged =
                            assignAuthorizationProfileToMediumResult
                                .mediumAuthorizationProfileChangedDeferred
                                .await()
                        MediumAuthorizationProfileChanged.id.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                        shouldThrow<OptionalEventException> {
                            assignAuthorizationProfileToMediumResult.await()
                        }
                    }
                }
            }

        test("assign autorization profile to medium with wrong publish back to get an Exception") {
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
                                Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    accessBeginAt =
                                        LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                    changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991")))

                        client
                            .publishAsync(
                                Topics.Event.MEDIUM_CHANGED,
                                "\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\"" /*encodeEvent(apiEvent)*/)
                            .await()

                        client
                            .publishAsync(
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                                encodeEvent(apiEvent2))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.MEDIUM_CHANGED,
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED))
                        .await()

                    val assignAuthorizationProfileToMediumResult =
                        api.assignAuthorizationProfileToMediumAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        )

                    shouldThrow<ParsingException> {
                        assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                    }
                    val mediumAuthorizationProfilechanged =
                        assignAuthorizationProfileToMediumResult
                            .mediumAuthorizationProfileChangedDeferred
                            .await()
                    mediumAuthorizationProfilechanged.id.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                }
            }
        }

        test("assign authorization profile to medium returning both events") {
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
                                Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent2 =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumAuthorizationProfileChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")))

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                MediumChanged(
                                    id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    accessBeginAt =
                                        LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                    changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991")))

                        client
                            .publishAsync(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
                            .await()

                        client
                            .publishAsync(
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                                encodeEvent(apiEvent2))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(
                            Topics(
                                Topics.Event.MEDIUM_CHANGED,
                                Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED))
                        .await()

                    val assignAuthorizationProfileToMediumResult =
                        api.assignAuthorizationProfileToMediumAsync(
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        )
                    val test =
                        assignAuthorizationProfileToMediumResult
                            .mediumAuthorizationProfileChangedDeferred
                            .await()
                    test.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    val mediumChanged =
                        assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                    mediumChanged.id.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                }
            }
        }

        test(
            "assign authorization profile to medium but only one optional field will be returned") {
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
                                    Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM -> {
                                        commandReceived.complete(payload.decodeToString())
                                    }
                                }
                            }

                            simulatedBackendReady.complete(Unit)

                            val commandContent = commandReceived.await()
                            commandContent.shouldBeEqual(
                                "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                            val apiEvent =
                                ApiEvent(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    MediumChanged(
                                        id =
                                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                        accessBeginAt =
                                            LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                        changedAt =
                                            LocalDateTime.parse("2023-08-23T16:25:52.225991")))

                            client
                                .publishAsync(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
                                .await()
                        }
                    }
                    launch {
                        simulatedBackendReady.await()

                        val api = XesarConnect.connectAndLoginAsync(config).await()
                        api.subscribeAsync(
                                Topics(
                                    Topics.Event.MEDIUM_CHANGED,
                                    Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED))
                            .await()

                        val assignAuthorizationProfileToMediumResult =
                            api.assignAuthorizationProfileToMediumAsync(
                                UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            )
                        val mediumAuthorizationProfileChanged =
                            assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                        mediumAuthorizationProfileChanged.id.shouldBeEqual(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                        shouldThrow<OptionalEventException> {
                            assignAuthorizationProfileToMediumResult
                                .mediumAuthorizationProfileChangedDeferred
                                .await()
                        }
                    }
                }
            }
    })
