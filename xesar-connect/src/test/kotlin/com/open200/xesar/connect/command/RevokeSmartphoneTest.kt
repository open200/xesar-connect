package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.revokeSmartphoneAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MediumRevoked
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.UUID
import kotlinx.coroutines.test.runTest

class RevokeSmartphoneTest :
    FunSpec({
        test("revoke smartphone medium") {
            val smartphoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REVOKE_SMARTPHONE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumRevoked(
                                aggregateId = smartphoneId,
                                mediumIdentifier = 123456L,
                                hasMasterKeyAccess = false,
                            ),
                        )

                    emitMessage(Topics.Event.MEDIUM_REVOKED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result = api.revokeSmartphoneAsync(smartphoneId).await()

                result.aggregateId.shouldBeEqual(smartphoneId)
                result.mediumIdentifier.shouldBeEqual(123456L)

                harness
                    .publishedPayload(Topics.Command.REVOKE_SMARTPHONE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
