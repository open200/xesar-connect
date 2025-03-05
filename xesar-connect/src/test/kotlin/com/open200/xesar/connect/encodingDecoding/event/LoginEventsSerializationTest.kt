package com.open200.xesar.connect.encodingDecoding.event

import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class LoginEventsSerializationTest :
    FunSpec({
        test("serialize LoggedIn") {
            val apiEvent =
                ApiEvent(UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"), LoggedIn("token"))

            encodeEvent(apiEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\",\"event\":{\"token\":\"token\"}}"
                )
        }

        test("deserialize LoggedIn") {
            val text =
                "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\",\"event\":{\"token\":\"token\"}}"

            decodeEvent<LoggedIn>(text)
                .shouldBeEqual(
                    ApiEvent(
                        UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                        LoggedIn("token"),
                    )
                )
        }

        test("deserialize UnauthorizedLoginAttempt") {
            val text = "{\"event\":{\"username\":\"fordprefect\",\"channel\":\"API\"}}"

            decodeEvent<UnauthorizedLoginAttempt>(text)
                .shouldBeEqual(
                    ApiEvent(
                        event =
                            UnauthorizedLoginAttempt(
                                "fordprefect",
                                UnauthorizedLoginAttempt.Channel.API,
                            )
                    )
                )
        }
    })
