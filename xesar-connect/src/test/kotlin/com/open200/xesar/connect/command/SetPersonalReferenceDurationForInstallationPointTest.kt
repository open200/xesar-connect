package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setPersonalReferenceDurationForInstallationPointAsync
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class SetPersonalReferenceDurationForInstallationPointTest :
    FunSpec({
        test("set personal reference duration for installation point") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(
                    Topics.Command.SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT
                ) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                personalReferenceDuration =
                                    PersonalLog(
                                        days = 30,
                                        logMode = PersonalLog.PersonalLogModes.saveForDays,
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.setPersonalReferenceDurationForInstallationPointAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            PersonalLog(
                                days = 30,
                                logMode = PersonalLog.PersonalLogModes.saveForDays,
                            ),
                        )
                        .await()
                result.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                result.personalReferenceDuration?.shouldBeEqual(
                    PersonalLog(days = 30, logMode = PersonalLog.PersonalLogModes.saveForDays)
                )

                harness
                    .publishedPayload(
                        Topics.Command.SET_PERSONAL_REFERENCE_DURATION_FOR_INSTALLATION_POINT
                    )
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"personalReferenceDuration\":{\"logMode\":\"saveForDays\",\"days\":30},\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
