package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.removeInstallationPointFromZoneAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointsInZoneChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class RemoveInstallationPointFromZoneTest :
    FunSpec({
        test("remove installation point from zone") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REMOVE_INSTALLATION_POINT_FROM_ZONE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointsInZoneChanged(
                                accessId = 123,
                                removedInstallationPoints =
                                    listOf(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")),
                                aggregateId =
                                    UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f"),
                            ),
                        )

                    emitMessage(
                        Topics.Event.INSTALLATION_POINTS_IN_ZONE_CHANGED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.removeInstallationPointFromZoneAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f"),
                        )
                        .await()
                result.aggregateId.shouldBeEqual(
                    UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f")
                )
                result.removedInstallationPoints.shouldBeEqual(
                    listOf(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                )

                harness
                    .publishedPayload(Topics.Command.REMOVE_INSTALLATION_POINT_FROM_ZONE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"id\":\"8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
