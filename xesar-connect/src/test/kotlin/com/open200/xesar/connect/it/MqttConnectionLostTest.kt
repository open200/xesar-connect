package com.open200.xesar.connect.it

import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perTest
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class MqttConnectionLostTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perTest())

        test("connection lost on container shutdown") {
            runBlocking {
                val connected = CompletableDeferred<Unit>()
                val connectionLost = CompletableDeferred<Unit>()
                launch {
                    val xesarMqttClient = XesarMqttClient.connectAsync(config).await()
                    connected.complete(Unit)

                    val xesarConnect = XesarConnect(xesarMqttClient, config)
                    xesarConnect.onConnectionLost = { cause -> connectionLost.complete(Unit) }
                    try {
                        xesarConnect.delay()
                    } catch (e: Exception) {
                        // ignore
                    }
                }
                launch {
                    withTimeout(1000) { connected.await() }
                    container.stop()
                    withTimeout(1000) { connectionLost.await() }
                }
            }
        }
    })
