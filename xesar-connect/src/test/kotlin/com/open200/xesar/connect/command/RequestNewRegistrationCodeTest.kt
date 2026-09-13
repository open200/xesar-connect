package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.requestNewRegistrationCodeAsync
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.coroutines.test.runTest

class RequestNewRegistrationCodeTest :
    FunSpec({
        test("request new registration code for a smartphone media") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REQUEST_NEW_REGISTRATION_CODE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            NewRegistrationCodeRequested(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )

                    emitMessage(Topics.Event.NEW_REGISTRATION_CODE_REQUESTED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val requestNewRegistrationCodeResult =
                    api.requestNewRegistrationCodeAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()

                requestNewRegistrationCodeResult.id.shouldBe(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                harness
                    .publishedPayload(Topics.Command.REQUEST_NEW_REGISTRATION_CODE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
