package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.assignPersonToMediumAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MediumPersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class AssignPersonToMediumTest :
    FunSpec({
        test("assign person to medium") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.ASSIGN_PERSON_TO_MEDIUM) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumPersonChanged(
                                oldPersonId =
                                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.MEDIUM_PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.assignPersonToMediumAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        )
                        .await()

                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.oldPersonId!!.shouldBeEqual(
                    UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")
                )

                harness
                    .publishedPayload(Topics.Command.ASSIGN_PERSON_TO_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"personId\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
