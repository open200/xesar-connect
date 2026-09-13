package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.confirmSmartphoneUpdateAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.SmartphoneUpdateConfirmed
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.UUID
import kotlinx.coroutines.test.runTest

class ConfirmSmartphoneUpdateTest :
    FunSpec({
        test("confirm smartphone update in Self Service Mode") {
            val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
            val transactionId = UUID.fromString("1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CONFIRM_SMARTPHONE_UPDATE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            SmartphoneUpdateConfirmed(
                                mediumId = smartphoneId,
                                transactionId = transactionId,
                            ),
                        )

                    emitMessage(Topics.Event.SMARTPHONE_UPDATE_CONFIRMED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result = api.confirmSmartphoneUpdateAsync(smartphoneId, transactionId).await()

                result.mediumId.shouldBeEqual(smartphoneId)
                result.transactionId.shouldBeEqual(transactionId)

                harness
                    .publishedPayload(Topics.Command.CONFIRM_SMARTPHONE_UPDATE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                            "\"mediumId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"," +
                            "\"transactionId\":\"1e4a12b3-3c5f-4a6e-9b7d-8f0e1d2c3b4a\"," +
                            "\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
