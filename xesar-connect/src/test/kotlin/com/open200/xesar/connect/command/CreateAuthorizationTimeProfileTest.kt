package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createAuthorizationTimeProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationTimeProfileCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.messages.query.TimeProfileType
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class CreateAuthorizationTimeProfileTest :
    FunSpec({
        test("create authorization time profile") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_AUTHORIZATION_TIME_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationTimeProfileCreated(
                                timeSeries = TimeProfileFixture.timeSeries,
                                name = "timeProfileName",
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                type = TimeProfileType.AUTHORIZATION_PROFILE,
                                validStandardTimeProfile = true,
                            ),
                        )

                    emitMessage(
                        Topics.Event.AUTHORIZATION_TIME_PROFILE_CREATED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.createAuthorizationTimeProfileAsync(
                            name = "timeProfileName",
                            timeProfileId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            timeSeries = TimeProfileFixture.timeSeries,
                        )
                        .await()
                result.name.shouldBeEqual("timeProfileName")
                result.type.shouldBeEqual(TimeProfileType.AUTHORIZATION_PROFILE)

                harness
                    .publishedPayload(Topics.Command.CREATE_AUTHORIZATION_TIME_PROFILE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[],\"name\":\"timeProfileName\",\"description\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
