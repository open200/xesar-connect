package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.configureReleaseDurationAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ConfigureReleaseDurationTest :
    FunSpec({
        test("configure release duration") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CONFIGURE_RELEASE_DURATION) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                releaseDurationShort = 10,
                                releaseDurationLong = 20,
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.configureReleaseDurationAsync(
                            10,
                            20,
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.releaseDurationShort?.shouldBeEqual(10)
                result.releaseDurationLong?.shouldBeEqual(20)

                harness
                    .publishedPayload(Topics.Command.CONFIGURE_RELEASE_DURATION)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"releaseDurationShort\":10,\"releaseDurationLong\":20,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
