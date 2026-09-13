package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.withdrawAuthorizationProfileFromMediumAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationProfileWithdrawnFromMedium
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class WithdrawAuthorizationProfileFromMediumTest :
    FunSpec({
        test("withdraw authorization profile from medium") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.WITHDRAW_AUTHORIZATION_PROFILE_FROM_MEDIUM) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationProfileWithdrawnFromMedium(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                withdrawnAt = LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                authorizationProfileId =
                                    UUID.fromString("00000000-0000-0000-0000-000000000001"),
                            ),
                        )

                    emitMessage(
                        Topics.Event.AUTHORIZATION_PROFILE_WITHDRAWN_FROM_MEDIUM,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.withdrawAuthorizationProfileFromMediumAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            null,
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.authorizationProfileId?.shouldBeEqual(
                    UUID.fromString("00000000-0000-0000-0000-000000000001")
                )

                harness
                    .publishedPayload(Topics.Command.WITHDRAW_AUTHORIZATION_PROFILE_FROM_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
