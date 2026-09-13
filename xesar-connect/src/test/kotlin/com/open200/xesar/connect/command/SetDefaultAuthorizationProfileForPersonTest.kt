package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setDefaultAuthorizationProfileForPersonAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class SetDefaultAuthorizationProfileForPersonTest :
    FunSpec({
        test("set default authorization profile for a person") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                firstName = "firstName",
                                lastName = "lastName",
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.setDefaultAuthorizationProfileForPersonAsync("EXT-123", null).await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.SET_DEFAULT_AUTHORIZATION_PROFILE_FOR_PERSON)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"externalId\":\"EXT-123\",\"id\":null,\"defaultAuthorizationProfileName\":null,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
