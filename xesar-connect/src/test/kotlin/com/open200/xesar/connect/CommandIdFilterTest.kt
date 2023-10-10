package com.open200.xesar.connect

import com.open200.xesar.connect.filters.CommandIdFilter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue
import java.util.*

class CommandIdFilterTest :
    FunSpec({
        val commandId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")

        test("should return true when commandId is valid") {
            CommandIdFilter(commandId)
                .filter("topic", "{\"commandId\":\"$commandId\"}")
                .shouldBeTrue()
        }

        test("should return false when commandId is invalid") {
            CommandIdFilter(commandId)
                .filter("topic", "{\"WrongComamndId\":\"$commandId\"}")
                .shouldBeFalse()
        }
    })
