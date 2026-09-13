package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changeAuthorizationTimeProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.AuthorizationTimeProfileChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ChangeAuthorizationTimeProfileTest :
    FunSpec({
        test("change authorization time profile") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_AUTHORIZATION_TIME_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            AuthorizationTimeProfileChanged(
                                timeSeries = TimeProfileFixture.timeSeries,
                                exceptionTimeSeries = TimeProfileFixture.exceptionTimeSerie,
                                name = "timeProfileName",
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                validStandardTimeProfile = true,
                            ),
                        )

                    emitMessage(
                        Topics.Event.AUTHORIZATION_TIME_PROFILE_CHANGED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.changeAuthorizationTimeProfileAsync(
                            timeProfileId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            timeProfileName = "timeProfileName",
                            timeSeries = TimeProfileFixture.timeSeries,
                            exceptionTimeSeries = TimeProfileFixture.exceptionTimeSerie,
                        )
                        .await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.exceptionTimeSeries.shouldBeEqual(TimeProfileFixture.exceptionTimeSerie)
                result.name.shouldBeEqual("timeProfileName")

                harness
                    .publishedPayload(Topics.Command.CHANGE_AUTHORIZATION_TIME_PROFILE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"name\":\"timeProfileName\",\"description\":null,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"calendars\":[\"497f6eca-6276-4993-bfeb-53cbbbba6f08\"]}],\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
