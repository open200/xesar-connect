package com.open200.xesar.connect

import com.open200.xesar.connect.filters.AllTopicsFilter
import com.open200.xesar.connect.testutils.MosquittoContainer
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.equals.shouldBeEqual
import java.lang.RuntimeException
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout

class ListenerTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("unregister should unregister listener") {
            withTimeout(5000) {
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        val it = XesarConnect.connectAndLoginAsync(config).await()
                        it.subscribeAsync(Topics("#")).await()

                        val firstEventReceived = CompletableDeferred<String>()
                        val secondEventReceived = CompletableDeferred<String>()
                        val thirdEventReceived = CompletableDeferred<String>()

                        val listener =
                            it.on(AllTopicsFilter()) {
                                if (!firstEventReceived.isCompleted) {
                                    firstEventReceived.complete(it.message)
                                    return@on
                                }
                                if (!secondEventReceived.isCompleted) {
                                    secondEventReceived.complete(it.message)
                                    return@on
                                }
                                throw RuntimeException("Unexpected event received in test")
                            }

                        // Make sure we are able to receive events at all
                        client.publishAsync("test", "test1").await()
                        firstEventReceived.await().shouldBeEqual("test1")

                        // Close and unregister the event listener
                        listener.close()

                        // Publish the second message, but expect it to not be handled by the
                        // previous listener
                        client.publishAsync("test", "test2").await()

                        // Add a new listener that only handles the test3 message
                        it.on(AllTopicsFilter()) {
                            if (it.message == "test3") {
                                thirdEventReceived.complete(it.message)
                            }
                        }

                        // Publish a third message, but only handle it by the third listener
                        client.publishAsync("test", "test3").await()

                        thirdEventReceived.await().shouldBeEqual("test3")
                        secondEventReceived.isCompleted.shouldBeFalse()
                    }
                }
            }
        }
    })
