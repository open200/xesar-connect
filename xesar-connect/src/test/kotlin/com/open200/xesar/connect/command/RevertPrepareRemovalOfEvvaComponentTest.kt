package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.revertPrepareRemovalOfEvvaComponentAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PrepareEvvaComponentRemovalReverted
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class RevertPrepareRemovalOfEvvaComponentTest :
    FunSpec({
        test("revert prepare removal of evva component") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PrepareEvvaComponentRemovalReverted(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                LocalDateTime.parse("2023-07-05T15:22:38.230076"),
                            ),
                        )

                    emitMessage(
                        Topics.Event.PREPARE_EVVA_COMPONENT_REMOVAL_REVERTED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.revertPrepareRemovalOfEvvaComponentAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.stateChangedAt.shouldBeEqual(
                    LocalDateTime.parse("2023-07-05T15:22:38.230076")
                )

                harness
                    .publishedPayload(Topics.Command.REVERT_PREPARE_REMOVAL_OF_EVVA_COMPONENT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
