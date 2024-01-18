package com.open200.xesar.connect

import com.open200.xesar.connect.filters.AllTopicsFilter
import com.open200.xesar.connect.testutils.MosquittoContainer
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.booleans.shouldBeTrue
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

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
                    XesarConnect.connectAndLoginAsync(config).await().use { client ->
                        client.subscribeAsync(Topics("#")).await()

                        client.on(AllTopicsFilter()) { client.close() }

                        waitingForMessage.complete(Unit)
                        client.delayUntilClose()

                        messageSend.isCompleted.shouldBeTrue()
                        messageSend.await().shouldBeTrue()

                        firstConnectionClosed.complete(true)
                    }
                }

                launch {
                    waitingForMessage.await()

                    XesarMqttClient.connectAsync(config).await().use { client ->
                        messageSend.complete(true)
                        client.publishAsync("test", "test1").await()
                    }
                }
            }

            firstConnectionClosed.await().shouldBeTrue()
        }
    })
