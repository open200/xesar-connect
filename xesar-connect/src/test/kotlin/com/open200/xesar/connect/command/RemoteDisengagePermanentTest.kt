package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.executeRemoteDisengagePermanentAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.RemoteDisengagePerformed
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.InstallationPointFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.http.*
import java.util.*
import kotlinx.coroutines.test.runTest

class RemoteDisengagePermanentTest :
    FunSpec({
        test("remote disengage permanent with success") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REMOTE_DISENGAGE_PERMANENT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            RemoteDisengagePerformed("ok"),
                        )

                    emitMessage(
                        Topics.Event.REMOTE_DISENGAGE_PERMANENT_PERFORMED,
                        encodeEvent(apiEvent),
                    )
                }

                val api = harness.api
                val result =
                    api.executeRemoteDisengagePermanentAsync(
                            InstallationPointFixture.installationPointFixture.id,
                            true,
                        )
                        .await()

                result.ok.shouldBeEqual("ok")

                harness
                    .publishedPayload(Topics.Command.REMOTE_DISENGAGE_PERMANENT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"enable\":true,\"token\":\"${MockedXesarConnect.TOKEN}\"}"
                    )
            }
        }
    })
