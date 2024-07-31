package com.open200.xesar.connect.it.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.XesarMqttClient
import com.open200.xesar.connect.extension.setPersonalReferenceDurationForInstallationPointAsync
import com.open200.xesar.connect.it.MosquittoContainer
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import io.kotest.common.runBlocking
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.testcontainers.perProject
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.coEvery
import java.util.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.launch

class SetPersonalReferenceDurationForInstallationPointTest :
    FunSpec({
        val container = MosquittoContainer.container()
        val config = MosquittoContainer.config(container)
        listener(container.perProject())

        test("set personal reference duration for installation point") {
            coEvery { config.uuidGenerator.generateId() }
                .returns(UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"))
            runBlocking {
                val simulatedBackendReady = CompletableDeferred<Unit>()
                val commandReceived = CompletableDeferred<String>()

                launch {
                    XesarMqttClient.connectAsync(config).await().use { client ->
                        client.subscribeAsync(arrayOf(Topics.ALL_TOPICS)).await()

                        client.onMessage = { topic, payload ->
                            when (topic) {
                                Topics.Command
                                    .SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT -> {
                                    commandReceived.complete(payload.decodeToString())
                                }
                            }
                        }

                        simulatedBackendReady.complete(Unit)

                        val commandContent = commandReceived.await()

                        commandContent.shouldBeEqual(
                            "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"personalReferenceDuration\":{\"logMode\":\"saveForDays\",\"days\":30},\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}")

                        val apiEvent =
                            ApiEvent(
                                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                                InstallationPointChanged(
                                    aggregateId =
                                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                    personalReferenceDuration =
                                        PersonalLog(
                                            days = 30,
                                            logMode = PersonalLog.PersonalLogModes.saveForDays)))

                        client
                            .publishAsync(
                                Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                            .await()
                    }
                }
                launch {
                    simulatedBackendReady.await()

                    val api = XesarConnect.connectAndLoginAsync(config).await()
                    api.subscribeAsync(Topics(Topics.Event.INSTALLATION_POINT_CHANGED)).await()
                    val result =
                        api.setPersonalReferenceDurationForInstallationPointAsync(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                PersonalLog(
                                    days = 30, logMode = PersonalLog.PersonalLogModes.saveForDays))
                            .await()
                    result.aggregateId.shouldBeEqual(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                    result.personalReferenceDuration?.shouldBeEqual(
                        PersonalLog(days = 30, logMode = PersonalLog.PersonalLogModes.saveForDays))
                }
            }
        }
    })
