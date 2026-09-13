package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.requestToAddMediumToInstallationAsync
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.coroutines.test.runTest

class RequestAddMediumToInstallationTest :
    FunSpec({
        test("request add medium to installation returning both events") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REQUEST_ADD_MEDIUM_TO_INSTALLATION) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AddMediumToInstallationRequested(
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                hardwareId = "hardwareId",
                            ),
                        )

                    val apiEvent2 =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumAddedToInstallation(
                                aggregateId =
                                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                "hardwareId",
                            ),
                        )

                    emitMessage(
                        Topics.Event.ADD_MEDIUM_TO_INSTALLATION_REQUESTED,
                        encodeEvent(apiEvent),
                    )

                    emitMessage(Topics.Event.MEDIUM_ADDED_TO_INSTALLATION, encodeEvent(apiEvent2))
                }

                val api = harness.api
                val requestToAddMediumToInstallationResult =
                    api.requestToAddMediumToInstallationAsync(
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        "hardwareId",
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                    )
                val mediumAddedToInstallation =
                    requestToAddMediumToInstallationResult.mediumAddedToInstallationDeferred.await()
                mediumAddedToInstallation.aggregateId.shouldBe(
                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")
                )
                val addMediumToInstallationRequested =
                    requestToAddMediumToInstallationResult.addMediumToInstallationRequestedDeferred
                        .await()
                addMediumToInstallationRequested.hardwareId.shouldBe("hardwareId")

                harness
                    .publishedPayload(Topics.Command.REQUEST_ADD_MEDIUM_TO_INSTALLATION)
                    .shouldBeEqual(
                        "{\"hardwareId\":\"hardwareId\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"terminalId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"label\":null,\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
