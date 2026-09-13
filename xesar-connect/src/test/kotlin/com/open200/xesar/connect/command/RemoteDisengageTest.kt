package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.executeRemoteDisengageAsync
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.InstallationPointFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.http.*
import java.util.*
import kotlinx.coroutines.test.runTest

class RemoteDisengageTest :
    FunSpec({
        test("remote disengage with success") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.REMOTE_DISENGAGE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            RemoteDisengagePerformed("ok"),
                        )
                    val remoteDisengagePerformed = encodeEvent(apiEvent)

                    emitMessage(Topics.Event.REMOTE_DISENGAGE_PERFORMED, remoteDisengagePerformed)
                }

                val api = harness.api
                val result =
                    api.executeRemoteDisengageAsync(
                            InstallationPointFixture.installationPointFixture.id,
                            true,
                        )
                        .await()

                result.ok.shouldBeEqual("ok")

                harness
                    .publishedPayload(Topics.Command.REMOTE_DISENGAGE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"extended\":true,\"token\":\"${MockedXesarConnect.TOKEN}\"}"
                    )
            }
        }
    })
