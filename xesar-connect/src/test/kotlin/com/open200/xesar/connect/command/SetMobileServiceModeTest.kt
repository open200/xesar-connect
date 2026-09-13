package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.setMobileServiceModeAsync
import com.open200.xesar.connect.messages.MobileServiceMode
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.UUID
import kotlinx.coroutines.test.runTest

class SetMobileServiceModeTest :
    FunSpec({
        test("set mobile service mode to SELF_SERVICE") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.SET_MOBILE_SERVICE_MODE) {
                    // Enums are serialized using @SerialName -> "xms"

                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PartitionChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                mobileServiceMode = MobileServiceMode.SELF_SERVICE,
                            ),
                        )

                    emitMessage(Topics.Event.PARTITION_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result = api.setMobileServiceModeAsync(MobileServiceMode.SELF_SERVICE).await()

                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.mobileServiceMode?.shouldBeEqual(MobileServiceMode.SELF_SERVICE)

                harness
                    .publishedPayload(Topics.Command.SET_MOBILE_SERVICE_MODE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"mobileServiceMode\":\"self-service\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
