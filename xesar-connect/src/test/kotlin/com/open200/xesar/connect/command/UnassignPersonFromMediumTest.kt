package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.unassignPersonFromMediumAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MediumPersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.nulls.shouldBeNull
import java.util.UUID
import kotlinx.coroutines.test.runTest

class UnassignPersonFromMediumTest :
    FunSpec({
        test("unassign person from medium") {
            val mediumId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.UNASSIGN_PERSON_FROM_MEDIUM) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumPersonChanged(
                                oldPersonId = null,
                                newPersonId = null,
                                id = mediumId,
                            ),
                        )

                    emitMessage(Topics.Event.MEDIUM_PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result = api.unassignPersonFromMediumAsync(mediumId).await()

                result.id.shouldBeEqual(mediumId)
                result.newPersonId.shouldBeNull()

                harness
                    .publishedPayload(Topics.Command.UNASSIGN_PERSON_FROM_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"mediumId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
