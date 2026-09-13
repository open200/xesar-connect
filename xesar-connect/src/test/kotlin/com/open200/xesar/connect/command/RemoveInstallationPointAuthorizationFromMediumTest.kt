package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.removeInstallationPointAuthorizationFromMediumAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.IndividualAuthorizationsDeleted
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class RemoveInstallationPointAuthorizationFromMediumTest :
    FunSpec({
        test("remove installation point authorization from medium") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(
                    Topics.Command.REMOVE_INSTALLATION_POINT_AUTHORIZATION_FROM_MEDIUM
                ) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            IndividualAuthorizationsDeleted(
                                listOf(UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f")),
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(
                        Topics.Event.INDIVIDUAL_AUTHORIZATIONS_DELETED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.removeInstallationPointAuthorizationFromMediumAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            UUID.fromString("8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f"),
                        )
                        .await()
                result.mediumId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )

                harness
                    .publishedPayload(
                        Topics.Command.REMOVE_INSTALLATION_POINT_AUTHORIZATION_FROM_MEDIUM
                    )
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"authorization\":\"8c7128d4-a30f-4aad-b5d2-d7b975c5cf8f\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
