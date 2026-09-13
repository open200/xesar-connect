package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setPersonalReferenceDurationInPersonAsync
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class SetPersonalReferenceDurationInPersonTest :
    FunSpec({
        test("set personal reference duration in person") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_PERSONAL_REFERENCE_DURATION_IN_PERSON) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonChanged(
                                firstName = "firstName",
                                lastName = "lastName",
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                personalReferenceDuration = PersonalLog(days = 30),
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.setPersonalReferenceDurationInPersonAsync(
                            "EXT-123",
                            personalReferenceDuration = PersonalLog(days = 30),
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.personalReferenceDuration?.shouldBeEqual(PersonalLog(days = 30))

                harness
                    .publishedPayload(Topics.Command.SET_PERSONAL_REFERENCE_DURATION_IN_PERSON)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"newValue\":{\"logMode\":null,\"days\":30},\"externalId\":\"EXT-123\",\"id\":null,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
