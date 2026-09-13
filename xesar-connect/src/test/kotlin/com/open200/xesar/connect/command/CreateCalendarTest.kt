package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createCalendarAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.CalendarCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDate
import java.util.*
import kotlinx.coroutines.test.runTest

class CreateCalendarTest :
    FunSpec({
        test("create calendar") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_CALENDAR) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            CalendarCreated(
                                UUID.fromString("905a9d0a-111f-44ad-a871-f9d0a24335f3"),
                                123,
                                "calendarName",
                                listOf(LocalDate.parse("2018-02-25")),
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.CALENDAR_CREATED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.createCalendarAsync(
                            "calendarName",
                            listOf(LocalDate.parse("2018-02-25")),
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.name.shouldBeEqual("calendarName")
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.CREATE_CALENDAR)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"name\":\"calendarName\",\"specialDays\":[\"2018-02-25\"],\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
