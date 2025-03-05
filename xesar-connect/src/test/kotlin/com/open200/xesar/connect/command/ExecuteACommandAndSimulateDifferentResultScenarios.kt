package com.open200.xesar.connect.command

import com.open200.xesar.connect.*
import com.open200.xesar.connect.exception.OptionalEventException
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.exception.RequiredEventException
import com.open200.xesar.connect.extension.addEvvaComponentAsync
import com.open200.xesar.connect.messages.ApiError
import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.encodeError
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.EvvaComponentAdded
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.messages.query.ComponentStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

/*
 * Mock MqttAsyncClient and create a spy for XesarMqttClient to simulate different result scenarios from the broker when executing a command.
 * - login test is done seperately that is why we can just initialize the token (no connectAndLoginAsync function used)
 * - simulate incoming message from broker with executing onMessage XesarMqttClient instance - that is why subscription not needed for this test
 */
class ExecuteACommandAndSimulateDifferentResultScenarios :
    FunSpec({
        test("execute command (addEvvaComponentAsync) expecting no event and an api error") {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

                coEvery {
                    xesarMqttClientMock.publishAsync(
                        Topics.Command.ADD_EVVA_COMPONENT,
                        any(),
                        any(),
                    )
                } returns CompletableDeferred<Unit>().apply { complete(Unit) }

                val configMock = mockk<Config>()
                val standardTestDispatcher = StandardTestDispatcher(testScheduler)
                coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                    standardTestDispatcher
                coEvery { configMock.uuidGenerator.generateId() }
                    .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
                coEvery { configMock.apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        token = "aToken",
                    )
                val api = XesarConnect(xesarMqttClientMock, configMock)

                api.token = configMock.apiProperties.token!!

                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        ComponentType.Cylinder,
                    )

                xesarMqttClientMock.onMessage(
                    Topics.Event.error(configMock.apiProperties.userId),
                    encodeError(
                            ApiError(
                                "reason",
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                123,
                            )
                        )
                        .encodeToByteArray(),
                )

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

        test(
            "execute command (addEvvaComponentAsync) expecting the required event and an error event"
        ) {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

                coEvery {
                    xesarMqttClientMock.publishAsync(
                        Topics.Command.ADD_EVVA_COMPONENT,
                        any(),
                        any(),
                    )
                } returns CompletableDeferred<Unit>().apply { complete(Unit) }

                val configMock = mockk<Config>()
                val standardTestDispatcher = StandardTestDispatcher(testScheduler)
                coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                    standardTestDispatcher
                coEvery { configMock.uuidGenerator.generateId() }
                    .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
                coEvery { configMock.apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        token = "aToken",
                    )
                val api = XesarConnect(xesarMqttClientMock, configMock)
                api.token = configMock.apiProperties.token!!

                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        ComponentType.Cylinder,
                    )

                xesarMqttClientMock.onMessage(
                    Topics.Event.INSTALLATION_POINT_CHANGED,
                    encodeEvent(
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                xesarMqttClientMock.onMessage(
                    Topics.Event.error(configMock.apiProperties.userId),
                    encodeError(
                            ApiError(
                                "reason",
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                123,
                            )
                        )
                        .encodeToByteArray(),
                )

                val installationPointChanged =
                    addEvvaComponentEventPair.installationPointChangedDeferred.await()
                installationPointChanged.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                val apiError = addEvvaComponentEventPair.apiErrorDeferred.await()
                apiError.get().reason.shouldBe("reason")
                shouldThrow<OptionalEventException> {
                    addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                }
            }
        }
        // does this case even exist?
        test(
            "execute command (addEvvaComponentAsync) expecting required event and an error event but switch order of events"
        ) {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

                coEvery {
                    xesarMqttClientMock.publishAsync(
                        Topics.Command.ADD_EVVA_COMPONENT,
                        any(),
                        any(),
                    )
                } returns CompletableDeferred<Unit>().apply { complete(Unit) }

                val configMock = mockk<Config>()
                val standardTestDispatcher = StandardTestDispatcher(testScheduler)
                coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                    standardTestDispatcher
                coEvery { configMock.uuidGenerator.generateId() }
                    .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
                coEvery { configMock.apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        token = "aToken",
                    )
                val api = XesarConnect(xesarMqttClientMock, configMock)

                api.token = configMock.apiProperties.token!!

                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        ComponentType.Cylinder,
                    )

                xesarMqttClientMock.onMessage(
                    Topics.Event.error(configMock.apiProperties.userId),
                    encodeError(
                            ApiError(
                                "reason",
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                123,
                            )
                        )
                        .encodeToByteArray(),
                )

                xesarMqttClientMock.onMessage(
                    Topics.Event.INSTALLATION_POINT_CHANGED,
                    encodeEvent(
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                val installationPointChanged =
                    addEvvaComponentEventPair.installationPointChangedDeferred.await()
                installationPointChanged.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                val apiError = addEvvaComponentEventPair.apiErrorDeferred.await()
                apiError.get().reason.shouldBe("reason")
                shouldThrow<OptionalEventException> {
                    addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                }
            }
        }

        test(
            "execute command (addEvvaComponentAsync) expecting unknown json message from broker to get a ParsingException"
        ) {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

                coEvery {
                    xesarMqttClientMock.publishAsync(
                        Topics.Command.ADD_EVVA_COMPONENT,
                        any(),
                        any(),
                    )
                } returns CompletableDeferred<Unit>().apply { complete(Unit) }

                val configMock = mockk<Config>()
                val standardTestDispatcher = StandardTestDispatcher(testScheduler)
                coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                    standardTestDispatcher
                coEvery { configMock.uuidGenerator.generateId() }
                    .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
                coEvery { configMock.apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        token = "aToken",
                    )
                val api = XesarConnect(xesarMqttClientMock, configMock)

                api.token = configMock.apiProperties.token!!

                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        ComponentType.Cylinder,
                    )

                xesarMqttClientMock.onMessage(
                    Topics.Event.INSTALLATION_POINT_CHANGED,
                    "\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\"".encodeToByteArray(),
                )

                xesarMqttClientMock.onMessage(
                    Topics.Event.EVVA_COMPONENT_ADDED,
                    encodeEvent(
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                EvvaComponentAdded(
                                    id = UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                    evvaComponentId =
                                        UUID.fromString("3a33c05b-133d-4b9d-a496-5d30dfd2d2c3"),
                                    type = ComponentType.Cylinder,
                                    stateChangedAt = LocalDateTime.MIN,
                                    status = ComponentStatus.AssembledPrepared,
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                shouldThrow<ParsingException> {
                    addEvvaComponentEventPair.installationPointChangedDeferred.await()
                }
                val evvaComponentAdded =
                    addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                evvaComponentAdded.type.shouldBe(ComponentType.Cylinder)
            }
        }

        test("execute command (addEvvaComponentAsync) expecting only required event") {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

                coEvery {
                    xesarMqttClientMock.publishAsync(
                        Topics.Command.ADD_EVVA_COMPONENT,
                        any(),
                        any(),
                    )
                } returns CompletableDeferred<Unit>().apply { complete(Unit) }

                val configMock = mockk<Config>()
                val standardTestDispatcher = StandardTestDispatcher(testScheduler)
                coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                    standardTestDispatcher
                coEvery { configMock.uuidGenerator.generateId() }
                    .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
                coEvery { configMock.apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        token = "aToken",
                    )
                val api = XesarConnect(xesarMqttClientMock, configMock)

                api.token = configMock.apiProperties.token!!

                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        ComponentType.Cylinder,
                    )

                xesarMqttClientMock.onMessage(
                    Topics.Event.INSTALLATION_POINT_CHANGED,
                    encodeEvent(
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                val installationPointChanged =
                    addEvvaComponentEventPair.installationPointChangedDeferred.await()
                installationPointChanged.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                shouldThrow<OptionalEventException> {
                    addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                }
            }
        }
        test("execute command (addEvvaComponentAsync) expecting required and optional event") {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

                coEvery {
                    xesarMqttClientMock.publishAsync(
                        Topics.Command.ADD_EVVA_COMPONENT,
                        any(),
                        any(),
                    )
                } returns CompletableDeferred<Unit>().apply { complete(Unit) }

                val configMock = mockk<Config>()
                val standardTestDispatcher = StandardTestDispatcher(testScheduler)
                coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                    standardTestDispatcher
                coEvery { configMock.uuidGenerator.generateId() }
                    .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
                coEvery { configMock.apiProperties } returns
                    Config.ApiProperties(
                        hostname = "hostname",
                        port = "1883",
                        userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        token = "aToken",
                    )
                val api = XesarConnect(xesarMqttClientMock, configMock)
                api.token = configMock.apiProperties.token!!

                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        ComponentType.Cylinder,
                    )

                xesarMqttClientMock.onMessage(
                    Topics.Event.INSTALLATION_POINT_CHANGED,
                    encodeEvent(
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                xesarMqttClientMock.onMessage(
                    Topics.Event.EVVA_COMPONENT_ADDED,
                    encodeEvent(
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                EvvaComponentAdded(
                                    id = UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                    evvaComponentId =
                                        UUID.fromString("3a33c05b-133d-4b9d-a496-5d30dfd2d2c3"),
                                    type = ComponentType.Cylinder,
                                    stateChangedAt = LocalDateTime.MIN,
                                    status = ComponentStatus.AssembledPrepared,
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                val installationPointChanged =
                    addEvvaComponentEventPair.installationPointChangedDeferred.await()
                installationPointChanged.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                val evvaComponentAdded =
                    addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                evvaComponentAdded.type.shouldBe(ComponentType.Cylinder)
            }
        }
    })
