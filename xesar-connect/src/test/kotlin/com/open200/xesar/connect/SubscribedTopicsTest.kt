package com.open200.xesar.connect

import com.open200.xesar.connect.testutils.MosquittoContainer
import com.open200.xesar.connect.testutils.XesarConnectTestHelper
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.shouldBe

class SubscribedTopicsTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("subscribeAsync should add the new topic to the subscribed topics") {
            XesarConnectTestHelper.connect(config).use { client ->
                client.subscribeAsync(Topics("new Topic")).await()

                client
                    .getSubscribedTopics()
                    .shouldBe(
                        listOf(
                            "xs3/1/faf3d0c4-1281-40ae-89d7-5c541d77a757/LoggedIn",
                            "xs3/1/ces/UnauthorizedLoginAttempt",
                            "xs3/1/ces/LoggedOut",
                            "new Topic"))
            }
        }

        test("subscribeAsync should not subscribe to an already subscribed topics") {
            XesarConnectTestHelper.connect(config).use { client ->
                client.subscribeAsync(Topics(Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT)).await()

                client
                    .getSubscribedTopics()
                    .shouldBe(
                        listOf(
                            "xs3/1/faf3d0c4-1281-40ae-89d7-5c541d77a757/LoggedIn",
                            "xs3/1/ces/UnauthorizedLoginAttempt",
                            "xs3/1/ces/LoggedOut"))
            }
        }

        test("removeSubscribedTopic should remove the topic from the subscribed topics") {
            XesarConnectTestHelper.connect(config).use { client ->
                client
                    .getSubscribedTopics()
                    .shouldBe(
                        listOf(
                            "xs3/1/faf3d0c4-1281-40ae-89d7-5c541d77a757/LoggedIn",
                            "xs3/1/ces/UnauthorizedLoginAttempt",
                            "xs3/1/ces/LoggedOut"))

                client.unsubscribeTopics(
                    Topics(Topics.Event.LOGGED_OUT, Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT))

                logger.info { "Subscribed topics: ${client.getSubscribedTopics()}" }

                client
                    .getSubscribedTopics()
                    .shouldBe(listOf("xs3/1/faf3d0c4-1281-40ae-89d7-5c541d77a757/LoggedIn"))
            }
        }
    })
