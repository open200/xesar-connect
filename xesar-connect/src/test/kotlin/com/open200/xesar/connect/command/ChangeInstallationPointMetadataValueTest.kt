package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.changeInstallationPointMetadataValueAsync
import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ChangeInstallationPointMetadataValueTest :
    FunSpec({
        test("change installation point metadata value") {
            val installationPointId = UUID.fromString("11111111-2222-3333-4444-555555555555")
            val metadataId = UUID.fromString("aaaaaaaa-0000-0000-0000-000000000001")
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CHANGE_INSTALLATION_POINT_METADATA_VALUE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                aggregateId = installationPointId,
                                entityMetadata =
                                    listOf(
                                        EntityMetadata(
                                            id = metadataId,
                                            name = "test",
                                            value = "Test Value",
                                        )
                                    ),
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.changeInstallationPointMetadataValueAsync(
                            id = installationPointId,
                            metadataId = metadataId,
                            value = "Test Value",
                        )
                        .await()

                result.aggregateId.shouldBeEqual(installationPointId)
                result.entityMetadata!!
                    .single { it.id == metadataId }
                    .value
                    ?.shouldBeEqual("Test Value")

                harness
                    .publishedPayload(Topics.Command.CHANGE_INSTALLATION_POINT_METADATA_VALUE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"11111111-2222-3333-4444-555555555555\",\"metadataId\":\"aaaaaaaa-0000-0000-0000-000000000001\",\"value\":\"Test Value\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
