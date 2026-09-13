package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.findComponentAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.FindComponentPerformed
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import com.open200.xesar.connect.util.fixture.InstallationPointFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.ktor.http.*
import java.util.*
import kotlinx.coroutines.test.runTest

class FindComponentTest :
    FunSpec({
        test("find Component with success") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.FIND_COMPONENT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            FindComponentPerformed("ok"),
                        )

                    emitMessage(Topics.Event.FIND_COMPONENT_PERFORMED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.findComponentAsync(
                            InstallationPointFixture.installationPointFixture.id,
                            true,
                        )
                        .await()

                result.ok.shouldBeEqual("ok")

                harness
                    .publishedPayload(Topics.Command.FIND_COMPONENT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPointId\":\"${InstallationPointFixture.installationPointFixture.id}\",\"enable\":true,\"token\":\"${MockedXesarConnect.TOKEN}\"}"
                    )
            }
        }
    })
