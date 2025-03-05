package com.open200.xesar.connect.encodingDecoding.command

import com.open200.xesar.connect.messages.command.Login
import com.open200.xesar.connect.messages.command.encodeCommand
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class LoginTest :
    FunSpec({
        test("encode login") {
            val commandId = UUID.fromString("6569fd2f-6f90-4ebd-b734-80ddc5b38f69")
            val cmd = Login(commandId, "fordprefect", "foobar")

            encodeCommand(cmd)
                .shouldBeEqual(
                    "{\"commandId\":\"6569fd2f-6f90-4ebd-b734-80ddc5b38f69\",\"username\":\"fordprefect\",\"password\":\"foobar\"}"
                )
        }
    })
