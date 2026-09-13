package com.open200.xesar.connect.query

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.queryCalendarById
import com.open200.xesar.connect.extension.queryCalendars
import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.messages.query.Calendar
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.QueryTestHelper
import com.open200.xesar.connect.util.fixture.CalendarFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class QueryCalendarTest :
    FunSpec({
        test("queryCalendarList without params") {
            val requestId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val calendar =
                        encodeQueryList(
                            QueryList(
                                requestId,
                                QueryList.Response(
                                    listOf(
                                        CalendarFixture.calendarFixture,
                                        CalendarFixture.calendarFixture.copy(
                                            id =
                                                UUID.fromString(
                                                    "4509ca29-9fd3-454f-9c98-fc0967fe3f66"
                                                ),
                                            partitionId =
                                                UUID.fromString(
                                                    "6b4399a0-21ce-4bee-ba43-e06e291248d2"
                                                ),
                                        ),
                                    ),
                                    2,
                                    2,
                                ),
                            )
                        )

                    emitMessage(Topics.Query.result(userId), calendar)
                }

                val api = harness.api
                val result = api.queryCalendars()
                result.totalCount.shouldBeEqual(2)
                result.data[0]
                    .partitionId
                    .shouldBeEqual(UUID.fromString("7b4399a0-21ce-4bee-ba43-e06e291248d2"))
                result.data[1]
                    .partitionId
                    .shouldBeEqual(UUID.fromString("6b4399a0-21ce-4bee-ba43-e06e291248d2"))

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(Calendar.QUERY_RESOURCE, requestId)
                    )
            }
        }

        test("queryCalendarById") {
            val requestId = UUID.fromString("00000000-1281-42c0-9a15-c5844850c748")
            runTest {
                val harness = MockedXesarConnect(this, requestId)
                harness.respondTo(Topics.Query.REQUEST) {
                    val calendar =
                        encodeQueryElement(QueryElement(requestId, CalendarFixture.calendarFixture))

                    emitMessage(Topics.Query.result(userId), calendar)
                }

                val api = harness.api
                val result = api.queryCalendarById(CalendarFixture.calendarFixture.id)
                result!!.id.shouldBeEqual(CalendarFixture.calendarFixture.id)
                result.name.shouldBeEqual("string")

                harness
                    .publishedPayload(Topics.Query.REQUEST)
                    .shouldBeEqual(
                        QueryTestHelper.createQueryRequest(
                            Calendar.QUERY_RESOURCE,
                            requestId,
                            CalendarFixture.calendarFixture.id,
                        )
                    )
            }
        }
    })
