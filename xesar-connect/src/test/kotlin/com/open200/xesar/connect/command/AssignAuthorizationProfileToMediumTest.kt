package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.assignAuthorizationProfileToMediumAsync
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class AssignAuthorizationProfileToMediumTest :
    FunSpec({
        test("assign authorization profile to medium returning both events") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM) {
                    val apiEvent2 =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumAuthorizationProfileChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )

                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                accessBeginAt = LocalDateTime.parse("2023-08-24T16:25:52.225991"),
                                changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991"),
                            ),
                        )

                    emitMessage(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))

                    emitMessage(
                        Topics.Event.MEDIUM_AUTHORIZATION_PROFILE_CHANGED,
                        encodeEvent(apiEvent2),
                    )
                }

                val api = harness.api
                val assignAuthorizationProfileToMediumResult =
                    api.assignAuthorizationProfileToMediumAsync(
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be")
                    )
                val test =
                    assignAuthorizationProfileToMediumResult
                        .mediumAuthorizationProfileChangedDeferred
                        .await()
                test.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                val mediumChanged =
                    assignAuthorizationProfileToMediumResult.mediumChangedDeferred.await()
                mediumChanged.id.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                harness
                    .publishedPayload(Topics.Command.ASSIGN_AUTHORIZATION_PROFILE_TO_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorizationProfileId\":null,\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
