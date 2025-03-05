package com.open200.xesar.connect.encodingDecoding.command

import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.messages.command.Query
import com.open200.xesar.connect.messages.command.encodeCommand
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class QueryCommandTest :
    FunSpec({
        test("encode Query") {
            val requestId = UUID.fromString("6569fd2f-6f90-4ebd-b734-80ddc5b38f69")
            val token = "token"
            val cmd =
                Query(
                    "6569fd2f-6f90-4ebd-b734-80ddc5b38f69",
                    requestId,
                    token,
                    UUID.fromString("b62ea781-dde6-436c-bdd1-eeea2a8d921a"),
                    Query.Params(
                        1,
                        1,
                        "sort",
                        "language",
                        listOf(Query.Params.Filter("field", FilterType.EQ, "value")),
                    ),
                )

            encodeCommand(cmd)
                .shouldBeEqual(
                    "{\"resource\":\"6569fd2f-6f90-4ebd-b734-80ddc5b38f69\",\"requestId\":\"$requestId\",\"token\":\"$token\"," +
                        "\"id\":\"b62ea781-dde6-436c-bdd1-eeea2a8d921a\",\"params\":{\"pageOffset\":1,\"pageLimit\":1,\"sort\":\"sort\",\"language\":\"language\"," +
                        "\"filters\":[{\"field\":\"field\",\"type\":\"eq\",\"value\":\"value\"}]}}"
                )
        }
    })
