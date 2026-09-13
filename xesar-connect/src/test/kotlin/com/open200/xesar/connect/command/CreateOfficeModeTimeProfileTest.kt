package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createOfficeModeTimeProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.OfficeModeTimeProfileCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class CreateOfficeModeTimeProfileTest :
    FunSpec({
        test("create office mode time profile") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_OFFICE_MODE_TIME_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            OfficeModeTimeProfileCreated(
                                name = "timeProfileName",
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                timeSeries = TimeProfileFixture.timeSeries,
                                exceptionTimeSeries = listOf(),
                                exceptionTimePointSeries = listOf(),
                                timePointSeries = listOf(),
                            ),
                        )

                    emitMessage(
                        Topics.Event.OFFICE_MODE_TIME_PROFILE_CREATED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.createOfficeModeTimeProfileAsync(
                            name = "timeProfileName",
                            timeSeries = TimeProfileFixture.timeSeries,
                            timeProfileId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.name.shouldBeEqual("timeProfileName")
                result.timeSeries.shouldBeEqual(TimeProfileFixture.timeSeries)

                harness
                    .publishedPayload(Topics.Command.CREATE_OFFICE_MODE_TIME_PROFILE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[],\"exceptionTimePointSeries\":[],\"name\":\"timeProfileName\",\"description\":null,\"timePointSeries\":[],\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
