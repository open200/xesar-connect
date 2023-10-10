package com.open200.xesar.connect

import com.open200.xesar.connect.filters.QueryIdFilter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.util.*

class QueryFilterTest :
    FunSpec({
        val requestId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")

        test("should return true when requestId is valid") {
            QueryIdFilter(requestId)
                .filter("topic", "{\"requestId\":\"$requestId\"}")
                .shouldBeTrue()
        }

        test("should return false when requestId is invalid") {
            QueryIdFilter(requestId)
                .filter("topic", "{\"WrongRequestId\":\"$requestId\"}")
                .shouldBeFalse()
        }
    })
