package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.prepareRemovalOfEvvaComponentAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.EvvaComponentRemovalPrepared
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class PrepareRemovalOfEvvaComponentTest :
    FunSpec({
        test("prepare removal of evva component") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.PREPARE_REMOVAL_OF_EVVA_COMPONENT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            EvvaComponentRemovalPrepared(
                                stateChangeAt = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.EVVA_COMPONENT_REMOVAL_PREPARED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.prepareRemovalOfEvvaComponentAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()
                result.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                harness
                    .publishedPayload(Topics.Command.PREPARE_REMOVAL_OF_EVVA_COMPONENT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
