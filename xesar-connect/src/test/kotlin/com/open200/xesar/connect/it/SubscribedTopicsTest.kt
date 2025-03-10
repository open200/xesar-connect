package com.open200.xesar.connect.it

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.logger
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.shouldBe
import java.util.*

class SubscribedTopicsTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("subscribeAsync should add the new topic to the subscribed topics") {
            val client = XesarConnect.connectAndLoginAsync(config).await()
            client.subscribeAsync(Topics("new Topic")).await()

            client
                .getSubscribedTopics()
                .shouldBe(
                    listOf(
                        Topics.Event.loggedIn(
                            UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
                        ),
                        Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                        Topics.Event.LOGGED_OUT,
                        Topics.Event.error(config.apiProperties.userId),
                        "new Topic",
                    )
                )
        }

        test("subscribeAsync should not subscribe to an already subscribed topics") {
            val client = XesarConnect.connectAndLoginAsync(config).await()
            client.subscribeAsync(Topics(Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)).await()

            client
                .getSubscribedTopics()
                .shouldBe(
                    listOf(
                        Topics.Event.loggedIn(
                            UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
                        ),
                        Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                        Topics.Event.LOGGED_OUT,
                        Topics.Event.error(config.apiProperties.userId),
                    )
                )
        }

        test("removeSubscribedTopic should remove the topic from the subscribed topics") {
            val client = XesarConnect.connectAndLoginAsync(config).await()
            client
                .getSubscribedTopics()
                .shouldBe(
                    listOf(
                        Topics.Event.loggedIn(
                            UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
                        ),
                        Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                        Topics.Event.LOGGED_OUT,
                        Topics.Event.error(config.apiProperties.userId),
                    )
                )

            client.unsubscribeTopics(
                Topics(Topics.Event.LOGGED_OUT, Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)
            )

            logger.info { "Subscribed topics: ${client.getSubscribedTopics()}" }

            client
                .getSubscribedTopics()
                .shouldBe(
                    listOf(
                        Topics.Event.loggedIn(
                            UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
                        ),
                        Topics.Event.error(config.apiProperties.userId),
                    )
                )
        }
    })
