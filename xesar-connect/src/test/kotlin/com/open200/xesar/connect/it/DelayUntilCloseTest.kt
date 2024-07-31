package com.open200.xesar.connect.it

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.filters.AllTopicsFilter
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.*

class DelayUntilCloseTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("test the delayUntilClose method") {
            val waitingForMessage = CompletableDeferred<Unit>()
            val messageSend = CompletableDeferred<Boolean>()
            val firstConnectionClosed = CompletableDeferred<Boolean>()

            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"))

            withTimeout(5000) {
                launch {
                    val client = XesarConnect.connectAndLoginAsync(config).await()
                    client.subscribeAsync(Topics("#")).await()

                    client.on(AllTopicsFilter()) {
                        CoroutineScope(coroutineContext).launch { client.breakOutOfDelay() }
                    }
                    waitingForMessage.complete(Unit)
                    client.delay()

                    messageSend.isCompleted.shouldBeTrue()
                    messageSend.await().shouldBeTrue()

                    firstConnectionClosed.complete(true)
                }

                launch {
                    waitingForMessage.await()
                    val client = XesarMqttClient.connectAsync(config).await()
                    client.publishAsync("test", "test1").await()
                    messageSend.complete(true)
                }
            }

            firstConnectionClosed.await().shouldBeTrue()
        }
        test("delayuntilClose") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"))

            val job1 = launch {
                val client = XesarConnect.connectAndLoginAsync(config).await()
                client.subscribeAsync(Topics("#")).await()
                client.delay()
            }
            launch {
                delay(1000)
                job1.cancel()
            }
        }
    })
