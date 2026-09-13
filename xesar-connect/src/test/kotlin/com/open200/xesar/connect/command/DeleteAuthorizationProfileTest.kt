package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.deleteAuthorizationProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationProfileDeleted
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class DeleteAuthorizationProfileTest :
    FunSpec({
        test("delete authorization profile") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.DELETE_AUTHORIZATION_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationProfileDeleted(
                                individual = false,
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.AUTHORIZATION_PROFILE_DELETED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.deleteAuthorizationProfileAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.individual.shouldBeFalse()

                harness
                    .publishedPayload(Topics.Command.DELETE_AUTHORIZATION_PROFILE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
