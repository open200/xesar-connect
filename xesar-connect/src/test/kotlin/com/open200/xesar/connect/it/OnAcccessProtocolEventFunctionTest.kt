package com.open200.xesar.connect.it

import com.open200.xesar.connect.*
import com.open200.xesar.connect.messages.query.AccessProtocolEvent
import com.open200.xesar.connect.messages.query.EventType
import com.open200.xesar.connect.util.fixture.AccessProtocolEventFixture
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

class OnAcccessProtocolEventFunctionTest :
    FunSpec({
        test("test onAccessProtocolEvent handling two access protocol events") {
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

                var accessProtocolEventInOnFunctionReceived =
                    CompletableDeferred<AccessProtocolEvent>()

                api.onAccessProtocolEvent(
                    listOf(EventType.NORMAL_OPENING, EventType.OPENING_NORMAL_SWITCH)
                ) { accessProtocolEvent ->
                    logger.info { "Received $accessProtocolEvent" }
                    accessProtocolEventInOnFunctionReceived.complete(accessProtocolEvent)
                }

                xesarMqttClientMock.onMessage(
                    Topics.Event.accessProtocolEventTopic(EventType.NORMAL_OPENING),
                    AccessProtocolEvent.encode(AccessProtocolEventFixture.accessProtocolEvent)
                        .encodeToByteArray(),
                )

                val normalOpeningEvent = accessProtocolEventInOnFunctionReceived.await()
                normalOpeningEvent.eventType.shouldBe(EventType.NORMAL_OPENING)

                accessProtocolEventInOnFunctionReceived = CompletableDeferred()

                xesarMqttClientMock.onMessage(
                    Topics.Event.accessProtocolEventTopic(EventType.OPENING_NORMAL_SWITCH),
                    AccessProtocolEvent.encode(
                            AccessProtocolEventFixture.accessProtocolEvent.copy(
                                eventType = EventType.OPENING_NORMAL_SWITCH
                            )
                        )
                        .encodeToByteArray(),
                )

                val openingNormalSwitchEvent = accessProtocolEventInOnFunctionReceived.await()
                openingNormalSwitchEvent.eventType.shouldBe(EventType.OPENING_NORMAL_SWITCH)
            }
        }
    })
