package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.deleteCodingStationAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.CodingStationDeleted
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class DeleteCodingStationTest :
    FunSpec({
        test("delete coding station") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.DELETE_CODING_STATION) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            CodingStationDeleted(
                                LocalDateTime.parse("2023-07-05T15:22:13.509825"),
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.CODING_STATION_DELETED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.deleteCodingStationAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.DELETE_CODING_STATION)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
