package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setDefaultSmartphoneValidityDurationAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class SetDefaultSmartphoneValidityDurationTest :
    FunSpec({
        test("set default smartphone validity duration for all smartphones") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_DEFAULT_SMARTPHONE_VALIDITIY_DURATION) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PartitionChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                smartphoneValidityDuration = 10,
                            ),
                        )

                    emitMessage(Topics.Event.PARTITION_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result = api.setDefaultSmartphoneValidityDurationAsync(10).await()
                result.smartphoneValidityDuration!!.shouldBeEqual(10)

                harness
                    .publishedPayload(Topics.Command.SET_DEFAULT_SMARTPHONE_VALIDITIY_DURATION)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"validityDuration\":10,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
