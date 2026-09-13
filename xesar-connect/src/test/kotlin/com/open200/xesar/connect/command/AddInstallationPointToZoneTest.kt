package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.addInstallationPointToZoneAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointsInZoneChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class AddInstallationPointToZoneTest :
    FunSpec({
        test("add installation point to zone") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.ADD_INSTALLATION_POINT_TO_ZONE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointsInZoneChanged(
                                123,
                                listOf(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")),
                                UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                            ),
                        )

                    emitMessage(
                        Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.addInstallationPointToZoneAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        )
                        .await()
                result.addedInstallationPoints.size.shouldBeEqual(1)
                result.aggregateId.shouldBeEqual(
                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")
                )
                result.accessId.shouldBeEqual(123)

                harness
                    .publishedPayload(Topics.Command.ADD_INSTALLATION_POINT_TO_ZONE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
