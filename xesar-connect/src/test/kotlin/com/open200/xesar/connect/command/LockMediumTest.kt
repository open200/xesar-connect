package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.lockMediumAsync
import com.open200.xesar.connect.messages.event.MediumLocked
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class LockMediumTest :
    FunSpec({
        test("lock medium") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.LOCK_MEDIUM) {
                    emitEvent(
                        Topics.Event.MEDIUM_LOCKED,
                        MediumLocked(
                            aggregateId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            hasMasterKeyAccess = false,
                            mediumIdentifier = 123L,
                        ),
                    )
                }

                val result =
                    harness.api
                        .lockMediumAsync(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                        .await()

                harness
                    .publishedPayload(Topics.Command.LOCK_MEDIUM)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
                result.mediumIdentifier.shouldBeEqual(123L)
                result.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
            }
        }
    })
