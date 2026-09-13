package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.deleteInstallationPointAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointDeleted
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class DeleteInstallationPointTest :
    FunSpec({
        test("delete installation point") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.DELETE_INSTALLATION_POINT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointDeleted(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                linkedInstallationPoints =
                                    listOf(UUID.fromString("dc6f8102-af8f-4e8d-afd4-2d6ed1f5722d")),
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_DELETED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.deleteInstallationPointAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.linkedInstallationPoints.shouldBeEqual(
                    listOf(UUID.fromString("dc6f8102-af8f-4e8d-afd4-2d6ed1f5722d"))
                )

                harness
                    .publishedPayload(Topics.Command.DELETE_INSTALLATION_POINT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
