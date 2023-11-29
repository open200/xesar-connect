package com.open200.xesar.connect.encodingDecoding.command

import com.open200.xesar.connect.messages.command.Logout
import com.open200.xesar.connect.messages.command.encodeCommand
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual

class LogoutTest :
    FunSpec({
        test("encode logout") {
            val token = "6569fd2f-6f90-4ebd-b734-80ddc5b38f69"
            val cmd = Logout(token)

            encodeCommand(cmd).shouldBeEqual("{\"token\":\"$token\"}")
        }
    })
