package com.open200.xesar.connect.it

import com.open200.xesar.connect.*
import com.open200.xesar.connect.extension.queryStreamPerson
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.fixture.PersonFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.toList

class QueryStreamTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("queryPersonStream test with offset = 0, pagelimit = 2") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                var queryReceived = CompletableDeferred<String>()
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Query.REQUEST -> {
                                    queryReceived.complete(payload.decodeToString())
                                    queryReceived = CompletableDeferred<String>()
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val queryContent = queryReceived.await()

                        queryContent.shouldBeEqual(
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\",\"id\":null,\"params\":{\"pageOffset\":0,\"pageLimit\":2,\"sort\":null,\"language\":null,\"filters\":null}}")

                        val firstQueryPersonResponse =
                            encodeQueryList(
                                QueryList(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    QueryList.Response(
                                        listOf(
                                            PersonFixture.personFixture,
                                            PersonFixture.personFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "f7019248-f7f9-4138-9af7-119e2e251408"),
                                                firstName = "firstname 2 String",
                                            ),
                                        ),
                                        3,
                                        3,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                firstQueryPersonResponse)
                            .await()

                        val secondQueryContent = queryReceived.await()
                        secondQueryContent.shouldBe(
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\",\"id\":null,\"params\":{\"pageOffset\":2,\"pageLimit\":2,\"sort\":null,\"language\":null,\"filters\":null}}")

                        val secondQueryPersonResponse =
                            encodeQueryList(
                                QueryList(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    QueryList.Response(
                                        listOf(
                                            PersonFixture.personFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "f1177dbd-b8fc-4044-943b-28262e755d15"),
                                                firstName = "firstname 3 String")),
                                        3,
                                        3,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                secondQueryPersonResponse)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val params = Query.Params(pageOffset = 0, pageLimit = 2)
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryStreamPerson(params)
                    result.toList().let {
                        it.size.shouldBe(3)
                        it[0].firstName.shouldBe("firstname String")
                        it[1].firstName.shouldBe("firstname 2 String")
                        it[2].firstName.shouldBe("firstname 3 String")
                    }
                }
            }
        }

        test("queryPersonStream test with offset = 0, pagelimit = 1") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                var queryReceived = CompletableDeferred<String>()
                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Query.REQUEST -> {
                                    queryReceived.complete(payload.decodeToString())
                                    queryReceived = CompletableDeferred<String>()
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val queryContent = queryReceived.await()

                        queryContent.shouldBeEqual(
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\",\"id\":null,\"params\":{\"pageOffset\":0,\"pageLimit\":1,\"sort\":null,\"language\":null,\"filters\":null}}")

                        val firstQueryPersonResponse =
                            encodeQueryList(
                                QueryList(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    QueryList.Response(
                                        listOf(
                                            PersonFixture.personFixture,
                                        ),
                                        3,
                                        3,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                firstQueryPersonResponse)
                            .await()

                        val secondQueryContent = queryReceived.await()
                        secondQueryContent.shouldBe(
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\",\"id\":null,\"params\":{\"pageOffset\":1,\"pageLimit\":1,\"sort\":null,\"language\":null,\"filters\":null}}")

                        val secondQueryPersonResponse =
                            encodeQueryList(
                                QueryList(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    QueryList.Response(
                                        listOf(
                                            PersonFixture.personFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "f7019248-f7f9-4138-9af7-119e2e251408"),
                                                firstName = "firstname 2 String",
                                            )),
                                        3,
                                        3,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                secondQueryPersonResponse)
                            .await()

                        val thirdQueryContent = queryReceived.await()
                        thirdQueryContent.shouldBe(
                            "{\"resource\":\"persons\",\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\",\"id\":null,\"params\":{\"pageOffset\":2,\"pageLimit\":1,\"sort\":null,\"language\":null,\"filters\":null}}")

                        val thirdQueryPersonResponse =
                            encodeQueryList(
                                QueryList(
                                    UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                    QueryList.Response(
                                        listOf(
                                            PersonFixture.personFixture.copy(
                                                id =
                                                    UUID.fromString(
                                                        "f1177dbd-b8fc-4044-943b-28262e755d15"),
                                                firstName = "firstname 3 String")),
                                        3,
                                        3,
                                    )))

                        client
                            .publishAsync(
                                Topics.Query.result(config.apiProperties.userId),
                                thirdQueryPersonResponse)
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()
                    val params = Query.Params(pageOffset = 0, pageLimit = 1)
                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Query.result(config.apiProperties.userId)))
                        .await()
                    val result = api.queryStreamPerson(params)
                    result.toList().let {
                        it.size.shouldBe(3)
                        it[0].firstName.shouldBe("firstname String")
                        it[1].firstName.shouldBe("firstname 2 String")
                        it[2].firstName.shouldBe("firstname 3 String")
                    }
                }
            }
        }
    })
