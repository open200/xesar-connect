package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setDisengagePeriodOnMediumAsync
import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MediumChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class SetDisengagePeriodOnMediumTest :
    FunSpec({
        test("set disengage period on medium") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_DISENGAGE_PERIOD_ON_MEDIUM) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991"),
                                disengagePeriod = DisengagePeriod.LONG,
                            ),
                        )

                    emitMessage(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.setDisengagePeriodOnMediumAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            DisengagePeriod.LONG,
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.disengagePeriod?.shouldBeEqual(DisengagePeriod.LONG)

                harness
                    .publishedPayload(Topics.Command.SET_DISENGAGE_PERIOD_ON_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"disengagePeriod\":\"LONG\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
