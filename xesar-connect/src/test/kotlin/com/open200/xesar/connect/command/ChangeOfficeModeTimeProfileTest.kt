package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changeOfficeModeTimeProfileAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.OfficeModeTimeProfileChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.TimeProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.coroutines.test.runTest

class ChangeOfficeModeTimeProfileTest :
    FunSpec({
        test("change office mode time profile") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_OFFICE_MODE_TIME_PROFILE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            OfficeModeTimeProfileChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                name = "name",
                                timeSeries = TimeProfileFixture.timeSeries,
                            ),
                        )

                    emitMessage(
                        Topics.Event.OFFICE_MODE_TIME_PROFILE_CHANGED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.changeOfficeModeTimeProfileAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            "name",
                            timeSeries = TimeProfileFixture.timeSeries,
                        )
                        .await()
                result.id.shouldBe(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.timeSeries.shouldBe(TimeProfileFixture.timeSeries)

                harness
                    .publishedPayload(Topics.Command.CHANGE_OFFICE_MODE_TIME_PROFILE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"name\":\"name\",\"description\":null,\"timeSeries\":[{\"times\":[{\"start\":\"14:15:00\",\"end\":\"14:15:00\"}],\"days\":[\"MONDAY\"]}],\"exceptionTimeSeries\":[],\"exceptionTimePointSeries\":[],\"timePointSeries\":[],\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
