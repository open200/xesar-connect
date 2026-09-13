package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.forceRemoveEvvaComponentAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.EvvaComponentRemoved
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class ForceRemoveEvvaComponentTest :
    FunSpec({
        test("force remove evva component") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.FORCE_REMOVE_EVVA_COMPONENT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            EvvaComponentRemoved(
                                accessId = 123,
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                useOddKey = true,
                                forced = true,
                                stateChangedAt = LocalDateTime.parse("2023-06-15T16:23:25.229593"),
                                nonce = 1,
                            ),
                        )

                    emitMessage(Topics.Event.EVVA_COMPONENT_REMOVED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.forceRemoveEvvaComponentAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()
                result.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                result.accessId.shouldBeEqual(123)

                harness
                    .publishedPayload(Topics.Command.FORCE_REMOVE_EVVA_COMPONENT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
