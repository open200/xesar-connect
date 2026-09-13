package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createAuthorizationProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationProfileCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.coroutines.test.runTest

class CreateAuthorizationProfileTest :
    FunSpec({
        test("create an authorization profile") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_AUTHORIZATION_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationProfileCreated(
                                true,
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                "authorizationProfile1",
                                "",
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                true,
                            ),
                        )

                    emitMessage(Topics.Event.AUTHORIZATION_PROFILE_CREATED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.createAuthorizationProfileAsync(
                            "authorizationProfile1",
                            "",
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.name.shouldBe("authorizationProfile1")
                result.partitionId.shouldBe(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.CREATE_AUTHORIZATION_PROFILE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"name\":\"authorizationProfile1\",\"description\":\"\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
