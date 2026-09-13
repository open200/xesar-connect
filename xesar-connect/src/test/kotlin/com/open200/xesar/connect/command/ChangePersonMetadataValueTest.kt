package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changePersonMetadataValueAsync
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.UUID
import kotlinx.coroutines.test.runTest

class ChangePersonMetadataValueTest :
    FunSpec({
        test("change person metadata value via externalId") {
            val externalId = "P-12345"
            val personId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_PERSON_METADATA_VALUE) {
                    // Order-sensitive raw JSON

                    // Simulate success PERSON_CHANGED with updated entity metadata
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonChanged(
                                firstName = "Tester",
                                lastName = "Testington",
                                id = personId,
                                entityMetadata =
                                    listOf(
                                        EntityMetadata(
                                            id = metadataId,
                                            name = "department",
                                            value = "Sales",
                                        )
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.changePersonMetadataValueAsync(
                            externalId = externalId,
                            id = null,
                            metadataId = metadataId,
                            value = "Sales",
                        )
                        .await()

                result.id.shouldBeEqual(personId)
                result.entityMetadata!!.single { it.id == metadataId }.value?.shouldBeEqual("Sales")

                harness
                    .publishedPayload(Topics.Command.CHANGE_PERSON_METADATA_VALUE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"externalId\":\"P-12345\",\"id\":null,\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Sales\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }

        test("change person metadata value via id") {
            val personId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("bbbbbbbb-0000-0000-0000-000000000002")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_PERSON_METADATA_VALUE) {
                    // Order-sensitive raw JSON

                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonChanged(
                                firstName = "Tester",
                                lastName = "Testington",
                                id = personId,
                                entityMetadata =
                                    listOf(
                                        EntityMetadata(
                                            id = metadataId,
                                            name = "team",
                                            value = "Team Blue",
                                        )
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.changePersonMetadataValueAsync(
                            id = personId,
                            metadataId = metadataId,
                            value = "Team Blue",
                        )
                        .await()

                result.id.shouldBeEqual(personId)
                result.entityMetadata!!
                    .single { it.id == metadataId }
                    .value
                    ?.shouldBeEqual("Team Blue")

                harness
                    .publishedPayload(Topics.Command.CHANGE_PERSON_METADATA_VALUE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"externalId\":null,\"id\":\"$personId\",\"metadataId\":\"$metadataId\",\"value\":\"Team Blue\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
