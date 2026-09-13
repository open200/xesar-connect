package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.configureMediaUpgradeMapiAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ConfigureMediaUpgradeTest :
    FunSpec({
        test("configure media upgrade") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CONFIGURE_MEDIA_UPGRADE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                upgradeMedia = true,
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.configureMediaUpgradeMapiAsync(
                            true,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.upgradeMedia?.shouldBeTrue()

                harness
                    .publishedPayload(Topics.Command.CONFIGURE_MEDIA_UPGRADE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"upgradeMedia\":true,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
