package com.open200.xesar.connect.encodingDecoding.event

import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class MobileRegistrationStateUpdatedSerializationTest :
    FunSpec({
        val id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
        val commandId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")

        test("serialize MobileRegistrationStateUpdated") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    MobileRegistrationStateUpdated(id = id, registrationState = "REGISTERED"),
                )

            encodeEvent(apiEvent)
                .shouldBeEqual(
                    "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                        "\"event\":{\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                        "\"registrationState\":\"REGISTERED\"}}"
                )
        }

        test("deserialize MobileRegistrationStateUpdated") {
            val text =
                "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\"," +
                    "\"event\":{\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                    "\"registrationState\":\"REGISTERED\"}}"

            decodeEvent<MobileRegistrationStateUpdated>(text)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        MobileRegistrationStateUpdated(id = id, registrationState = "REGISTERED"),
                    )
                )
        }
    })
