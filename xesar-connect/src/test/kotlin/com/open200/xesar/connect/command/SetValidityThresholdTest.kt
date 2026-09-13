package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setValidityThresholdAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class SetValidityThresholdTest :
    FunSpec({
        test("set validity threshold") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_VALIDITY_THRESHOLD) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PartitionChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                validityDuration = 123,
                            ),
                        )

                    emitMessage(Topics.Event.PARTITION_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result = api.setValidityThresholdAsync(123).await()
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.validityDuration?.shouldBeEqual(123)

                harness
                    .publishedPayload(Topics.Command.SET_VALIDITY_THRESHOLD)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"validityThreshold\":123,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
