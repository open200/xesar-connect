package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setDefaultDisengagePeriodForPersonAsync
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class SetDefaultDisengagePeriodForPersonTest :
    FunSpec({
        test("set default disengage period for a person") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                firstName = "firstName",
                                lastName = "lastName",
                                disengagePeriod = DisengagePeriod.SHORT,
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.setDefaultDisengagePeriodForPersonAsync(
                            "EXT-123",
                            disengagePeriod = DisengagePeriod.SHORT,
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.disengagePeriod?.shouldBeEqual(DisengagePeriod.SHORT)

                harness
                    .publishedPayload(Topics.Command.SET_DEFAULT_DISENGAGE_PERIOD_FOR_PERSON)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"disengagePeriod\":\"SHORT\",\"externalId\":\"EXT-123\",\"id\":null,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
