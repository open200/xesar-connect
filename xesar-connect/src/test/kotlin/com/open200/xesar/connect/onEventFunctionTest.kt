package com.open200.xesar.connect

import com.open200.xesar.connect.filters.EventAndCommandIdFilter
import com.open200.xesar.connect.filters.TopicFilter
import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

class onEventFunctionTest :
    FunSpec({
        test("test onEvent function with two different MessageFilters and two different events") {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit

                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))

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

                val unauthorizedLoginAttemptEventReceived =
                    CompletableDeferred<ApiEvent<UnauthorizedLoginAttempt>>()

                api.onEvent<UnauthorizedLoginAttempt>(
                    TopicFilter(Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)
                ) { apiEvent ->
                    logger.info { "Received $apiEvent" }
                    unauthorizedLoginAttemptEventReceived.complete(apiEvent)
                }

                xesarMqttClientMock.onMessage(
                    Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                    encodeEvent(
                            ApiEvent(
                                UUID.randomUUID(),
                                UnauthorizedLoginAttempt(
                                    "username",
                                    UnauthorizedLoginAttempt.Channel.API,
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                val apiEvent = unauthorizedLoginAttemptEventReceived.await()
                apiEvent.event.username.shouldBe("username")

                val codingStationChangedEvent =
                    CompletableDeferred<ApiEvent<CodingStationChanged>>()

                val commandId = UUID.randomUUID()
                api.onEvent<CodingStationChanged>(
                    EventAndCommandIdFilter(commandId, Topics.Event.CODING_STATION_CHANGED)
                ) {
                    logger.info { "Received $it" }
                    codingStationChangedEvent.complete(it)
                }

                xesarMqttClientMock.onMessage(
                    Topics.Event.CODING_STATION_CHANGED,
                    encodeEvent(
                            ApiEvent(
                                commandId,
                                CodingStationChanged(
                                    "codingstation name",
                                    "new description",
                                    UUID.randomUUID(),
                                ),
                            )
                        )
                        .encodeToByteArray(),
                )

                val codingStationChanged = codingStationChangedEvent.await()
                codingStationChanged.event.name.shouldBe("codingstation name")
            }
        }
    })
