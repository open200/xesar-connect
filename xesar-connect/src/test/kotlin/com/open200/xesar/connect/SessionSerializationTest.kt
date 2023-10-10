package com.open200.xesar.connect

import com.open200.xesar.connect.messages.session.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class SessionSerializationTest :
    FunSpec({
        test("serialize LoggedIn") {
            val loggedIn = LoggedIn("token")
            val sessionEvent =
                Session(UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"), loggedIn)

            encodeSession(sessionEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\",\"event\":{\"token\":\"token\"}}")
        }

        test("deserialize LoggedIn") {
            val text =
                "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\",\"event\":{\"token\":\"token\"}}"

            decodeSession<LoggedIn>(text)
                .shouldBeEqual(
                    Session(
                        UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"), LoggedIn("token")))
        }

        test("deserialize UnauthorizedLoginAttempt") {
            val text = "{\"event\":{\"username\":\"fordprefect\",\"channel\":\"channel\"}}"

            decodeUnauthorizedLoginAttempt(text)
                .shouldBeEqual(
                    UnauthorizedLoginAttempt(
                        UnauthorizedLoginAttempt.Event("fordprefect", "channel")))
        }
    })
