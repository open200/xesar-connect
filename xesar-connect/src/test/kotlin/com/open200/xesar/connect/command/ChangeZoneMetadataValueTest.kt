package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changeZoneMetadataValueAsync
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.ZoneChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ChangeZoneMetadataValueTest :
    FunSpec({
        test("change zone metadata value") {
            val zoneId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_ZONE_METADATA_VALUE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            ZoneChanged(
                                name = "Test Zone",
                                description = "Test Description",
                                id = zoneId,
                                entityMetadata =
                                    listOf(
                                        EntityMetadata(
                                            id = metadataId,
                                            name = "security clearance required",
                                            value = "Top Secret",
                                        )
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.ZONE_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.changeZoneMetadataValueAsync(
                            id = zoneId,
                            metadataId = metadataId,
                            value = "Top Secret",
                        )
                        .await()

                result.id.shouldBeEqual(zoneId)
                result.entityMetadata!!
                    .single { it.id == metadataId }
                    .value
                    ?.shouldBeEqual("Top Secret")

                harness
                    .publishedPayload(Topics.Command.CHANGE_ZONE_METADATA_VALUE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"11111111-2222-3333-4444-555555555555\",\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Top Secret\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
