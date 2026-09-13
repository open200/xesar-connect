package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.deleteEntityMetadataDefinitionAsync
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.EntityType
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PartitionChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class DeleteEntityMetadataDefinitionTest :
    FunSpec({
        test("delete entity metadata definition") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.DELETE_ENTITY_METADATA_DEFINITION) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PartitionChanged(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                entityMetadataDefinitions =
                                    PartitionChanged.EntityMetadataDefinitions(
                                        authorizationProfiles = emptyList(),
                                        identificationMedia = emptyList(),
                                        installationPoints = emptyList(),
                                        persons =
                                            listOf(
                                                EntityMetadata(
                                                    id =
                                                        UUID.fromString(
                                                            "aaaaaaaa-0000-0000-0000-000000000001"
                                                        ),
                                                    name = "nickname",
                                                )
                                            ),
                                        zones = emptyList(),
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.PARTITION_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.deleteEntityMetadataDefinitionAsync(EntityType.PERSON, "department").await()

                result.id.shouldBeEqual(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                result.entityMetadataDefinitions!!
                    .persons
                    .map { it.name }
                    .shouldBeEqual(listOf("nickname"))

                harness
                    .publishedPayload(Topics.Command.DELETE_ENTITY_METADATA_DEFINITION)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"entityType\":\"PERSON\",\"names\":[\"department\"],\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
