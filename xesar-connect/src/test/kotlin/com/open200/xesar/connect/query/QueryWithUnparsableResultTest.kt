package com.open200.xesar.connect.query

import com.open200.xesar.connect.*
import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.extension.queryEvvaComponentById
import com.open200.xesar.connect.extension.queryEvvaComponents
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.async
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import org.eclipse.paho.client.mqttv3.MqttAsyncClient

/*
 * Mock MqttAsyncClient and create a spy for XesarMqttClient to simulate a query result from the broker which can't be decoded.
 * The query must fail with a ParsingException containing the original cause instead of running into a timeout.
 * Kotlin coroutines may add a copy of the exception for stack trace recovery (e.g. when assertions are enabled),
 * that is why the whole cause chain is checked for the original SerializationException.
 */
class QueryWithUnparsableResultTest :
    FunSpec({
        val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
        val userId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")

        fun TestScope.createApi(xesarMqttClientMock: XesarMqttClient): XesarConnect {
            coEvery { xesarMqttClientMock.publishAsync(Topics.Query.REQUEST, any(), any()) } returns
                CompletableDeferred<Unit>().apply { complete(Unit) }

            val configMock = mockk<Config>()
            coEvery { configMock.dispatcherForCommandsAndCleanUp } returns
                StandardTestDispatcher(testScheduler)
            coEvery { configMock.uuidGenerator.generateId() }.returns(requestId)
            coEvery { configMock.apiProperties } returns
                Config.ApiProperties(
                    hostname = "hostname",
                    port = "1883",
                    userId = userId,
                    token = "aToken",
                )
            return XesarConnect(xesarMqttClientMock, configMock).apply {
                token = configMock.apiProperties.token!!
            }
        }

        fun Throwable.hasCauseOfType(type: Class<out Throwable>): Boolean =
            generateSequence(cause) { it.cause }.any { type.isInstance(it) }

        test("query a list with an unparsable result throws a ParsingException with the cause") {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit
                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))
                val api = createApi(xesarMqttClientMock)

                val result = async { runCatching { api.queryEvvaComponents() } }
                runCurrent()

                xesarMqttClientMock.onMessage(
                    Topics.Query.result(userId),
                    ("{\"requestId\":\"$requestId\",\"response\":{\"data\":[{\"componentType\":\"WallReader\"}],\"totalCount\":1,\"filterCount\":1}}")
                        .encodeToByteArray(),
                )

                val exception =
                    result.await().exceptionOrNull().shouldBeInstanceOf<ParsingException>()
                exception.hasCauseOfType(SerializationException::class.java).shouldBeTrue()
            }
        }

        test(
            "query an element with an unparsable result throws a ParsingException with the cause"
        ) {
            runTest {
                val mqttAsyncClient = mockk<MqttAsyncClient>()
                coEvery { mqttAsyncClient.setCallback(any()) } returns Unit
                val xesarMqttClientMock = spyk(XesarMqttClient(mqttAsyncClient))
                val api = createApi(xesarMqttClientMock)

                val result = async {
                    runCatching {
                        api.queryEvvaComponentById(
                            UUID.fromString("497f6eca-6276-4993-bfeb-53cbbbba6f08")
                        )
                    }
                }
                runCurrent()

                xesarMqttClientMock.onMessage(
                    Topics.Query.result(userId),
                    ("{\"requestId\":\"$requestId\",\"response\":{\"componentType\":\"WallReader\"}}")
                        .encodeToByteArray(),
                )

                val exception =
                    result.await().exceptionOrNull().shouldBeInstanceOf<ParsingException>()
                exception.hasCauseOfType(SerializationException::class.java).shouldBeTrue()
            }
        }
    })
