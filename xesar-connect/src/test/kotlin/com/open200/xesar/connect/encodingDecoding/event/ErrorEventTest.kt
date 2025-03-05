package com.open200.xesar.connect.encodingDecoding.event

import com.open200.xesar.connect.messages.ApiError
import com.open200.xesar.connect.messages.encodeError
import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.http.*
import java.util.*

class ErrorEventTest :
    FunSpec({
        test("serialize ErrorEvent") {
            val apiError =
                ApiError(
                    correlationId = UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                    error = HttpStatusCode.Forbidden.value,
                )

            encodeError(apiError)
                .shouldBeEqual(
                    "{\"reason\":null,\"correlationId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"error\":403}"
                )
        }
    })
