package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changeMediumMetadataValueAsync
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.MediumChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDateTime
import java.util.UUID
import kotlinx.coroutines.test.runTest

class ChangeMediumMetadataValueTest :
    FunSpec({
        test("change medium metadata value") {
            val mediumId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_MEDIUM_METADATA_VALUE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            MediumChanged(
                                id = mediumId,
                                changedAt = LocalDateTime.parse("2023-08-23T16:25:52.225991"),
                                entityMetadata =
                                    listOf(
                                        EntityMetadata(
                                            id = metadataId,
                                            name = "Group",
                                            value = "Vogons",
                                        )
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.MEDIUM_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.changeMediumMetadataValueAsync(
                            id = mediumId,
                            metadataId = metadataId,
                            value = "Vogons",
                        )
                        .await()

                result.id.shouldBeEqual(mediumId)
                result.entityMetadata!!
                    .single { it.id == metadataId }
                    .value
                    ?.shouldBeEqual("Vogons")

                harness
                    .publishedPayload(Topics.Command.CHANGE_MEDIUM_METADATA_VALUE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"11111111-2222-3333-4444-555555555555\",\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Vogons\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
