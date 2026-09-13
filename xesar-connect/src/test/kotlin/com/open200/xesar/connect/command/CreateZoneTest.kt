package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createZoneAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.ZoneCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class CreateZoneTest :
    FunSpec({
        test("create zone") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_ZONE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            ZoneCreated(
                                123,
                                UUID.fromString("dc6f8102-af8f-4e8d-afd4-2d6ed1f5722d"),
                                "zoneName",
                                null,
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.ZONE_CREATED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.createZoneAsync(
                            name = "zoneName",
                            zoneId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            installationPoints =
                                listOf(
                                    UUID.fromString("720eb694-a085-47e8-8f18-512aa1a63bef"),
                                    UUID.fromString("c457a028-dd08-4040-a1bb-17767e2b2c28"),
                                ),
                        )
                        .await()
                result.name.shouldBeEqual("zoneName")
                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))

                harness
                    .publishedPayload(Topics.Command.CREATE_ZONE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"installationPoints\":[\"720eb694-a085-47e8-8f18-512aa1a63bef\",\"c457a028-dd08-4040-a1bb-17767e2b2c28\"],\"name\":\"zoneName\",\"description\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
