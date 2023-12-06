package com.open200.xesar.connect.encodingDecoding.event

import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.http.*
import java.util.*

class ErrorEventTest :
    FunSpec({
        test("serialize ErrorEvent") {
            val apiEvent =
                ApiEvent(
                    UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757"),
                    ErrorEvent(
                        correlationId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                        error = HttpStatusCode.Forbidden.value))

            encodeEvent(apiEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\",\"event\":{\"reason\":null,\"correlationId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"error\":403}}")
        }
    })
