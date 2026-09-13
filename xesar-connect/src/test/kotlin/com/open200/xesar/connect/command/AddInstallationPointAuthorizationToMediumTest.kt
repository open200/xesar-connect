package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.addInstallationPointAuthorizationToMediumAsync
import com.open200.xesar.connect.messages.command.AuthorizationData
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.IndividualAuthorizationsAddedToMedium
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class AddInstallationPointAuthorizationToMediumTest :
    FunSpec({
        test("add installation point authorization to medium") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.ADD_INSTALLATION_POINT_AUTHORIZATION_TO_MEDIUM) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            IndividualAuthorizationsAddedToMedium(
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )

                    emitMessage(
                        Topics.Event.INDIVIDUAL_AUTHORIZATIONS_ADDED_TO_MEDIUM,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val authorizationData =
                    AuthorizationData(
                        "authorizationName",
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        "timeProfileName",
                        UUID.fromString("8c124504-8263-4201-8b17-f49a6c2f8671"),
                        true,
                        UUID.fromString("e9b31e62-8969-4794-a219-8c81ff10c91d"),
                    )
                val result =
                    api.addInstallationPointAuthorizationToMediumAsync(
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        authorizationData,
                    )
                result
                    .await()
                    .id
                    .shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.ADD_INSTALLATION_POINT_AUTHORIZATION_TO_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"authorization\":{\"authorizationName\":\"authorizationName\",\"authorizationProfileId\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"timeProfileName\":\"timeProfileName\",\"authorizationId\":\"8c124504-8263-4201-8b17-f49a6c2f8671\",\"installationPoint\":true,\"timeProfileId\":\"e9b31e62-8969-4794-a219-8c81ff10c91d\"},\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
