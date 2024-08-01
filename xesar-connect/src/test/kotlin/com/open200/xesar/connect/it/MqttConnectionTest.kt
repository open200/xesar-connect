package com.open200.xesar.connect.it

import com.open200.xesar.connect.XesarMqttClient
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.shouldBe
import java.util.*

class MqttConnectionTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("connect with custom mqtt client id") {
            runBlocking {
                val mqttClientId = "test-client${UUID.randomUUID()}"

                val xesarMqttClient =
                    XesarMqttClient.connectAsync(config.copy(mqttClientId = mqttClientId)).await()
                xesarMqttClient.getClientId().shouldBe(mqttClientId)
            }
        }
    })
