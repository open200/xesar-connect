package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changePersonInformationAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ChangePersonInformationTest :
    FunSpec({
        test("change person information") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_PERSON_INFORMATION) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                firstName = "first name",
                                lastName = "last name",
                                identifier = "",
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.changePersonInformationAsync(
                            firstName = "first name",
                            lastName = "last name",
                            identifier = "",
                            externalId = "EXT-4711",
                        )
                        .await()
                result.firstName.shouldBeEqual("first name")
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.CHANGE_PERSON_INFORMATION)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"firstName\":\"first name\",\"lastName\":\"last name\",\"identifier\":\"\",\"externalId\":\"EXT-4711\",\"id\":null,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
