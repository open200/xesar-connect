package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.configureAssignableAuthorizationProfilesAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.UserGroupChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ConfigureAssignableAuthorizationProfilesTest :
    FunSpec({
        test("configure assignable authorization profiles") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CONFIGURE_ASSIGNABLE_AUTHORIZATION_PROFILES) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            UserGroupChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                name = "name",
                                description = "description",
                                assignableAuthorizationProfiles =
                                    listOf(UUID.fromString("4e6f78d6-51c7-4bc2-a992-78971eecfbda")),
                            ),
                        )

                    emitMessage(Topics.Event.USER_GROUP_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.configureAssignableAuthorizationProfilesAsync(
                            listOf(UUID.fromString("4e6f78d6-51c7-4bc2-a992-78971eecfbda")),
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.CONFIGURE_ASSIGNABLE_AUTHORIZATION_PROFILES)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"assignableAuthorizationProfiles\":[\"4e6f78d6-51c7-4bc2-a992-78971eecfbda\"],\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
