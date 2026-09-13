package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changeAuthorizationProfileAsync
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.coroutines.test.runTest

class ChangeAuthorizationProfileTest :
    FunSpec({
        test("change authorization profile returning all events") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_AUTHORIZATION_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationProfileChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )

                    val apiEvent2 =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationProfileAccessChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )
                    val apiEvent3 =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationProfileInfoChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )

                    emitMessage(Topics.Event.AUTHORIZATION_PROFILE_CHANGED, encodeEvent(apiEvent))

                    emitMessage(
                        Topics.Event.AUTHORIZATION_PROFILE_ACCESS_CHANGED,
                        encodeEvent(apiEvent2),
                    )

                    emitMessage(
                        Topics.Event.AUTHORIZATION_PROFILE_INFO_CHANGED,
                        encodeEvent(apiEvent3),
                    )
                }

                val api = harness.api
                val changeAuthorizationProfileResult =
                    api.changeAuthorizationProfileAsync(
                        emptyList(),
                        true,
                        "new name",
                        "new description",
                        null,
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        emptyList(),
                    )
                val authorizationProfileInfoChanged =
                    changeAuthorizationProfileResult.authorizationProfileInfoChangedDeferred.await()

                authorizationProfileInfoChanged.id.shouldBe(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                val authorizationProfileChanged =
                    changeAuthorizationProfileResult.authorizationProfileChangedDeferred.await()
                authorizationProfileChanged.id.shouldBe(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                val authorizationProfileAccessChanged =
                    changeAuthorizationProfileResult.authorizationProfileAccessChangedDeferred
                        .await()
                authorizationProfileAccessChanged.id.shouldBe(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                harness
                    .publishedPayload(Topics.Command.CHANGE_AUTHORIZATION_PROFILE)
                    .shouldBeEqual(
                        "{\"installationPoints\":[],\"manualOfficeMode\":true,\"name\":\"new name\",\"description\":\"new description\",\"standardTimeProfile\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"zones\":[],\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
