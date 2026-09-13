package com.open200.xesar.connect.command

import com.open200.xesar.connect.*
import com.open200.xesar.connect.extension.addEvvaComponentAsync
import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.messages.event.*
import com.open200.xesar.connect.messages.query.ComponentStatus
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.util.*
import kotlinx.coroutines.test.runTest

class AddEvvaComponentTest :
    FunSpec({
        test("add evva component returning both events") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.ADD_EVVA_COMPONENT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                            ),
                        )

                    val apiEvent2 =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            EvvaComponentAdded(
                                id = UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                                evvaComponentId =
                                    UUID.fromString("3a33c05b-133d-4b9d-a496-5d30dfd2d2c3"),
                                type = ComponentType.Cylinder,
                                stateChangedAt = LocalDateTime.MIN,
                                status = ComponentStatus.AssembledPrepared,
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))

                    emitMessage(Topics.Event.EVVA_COMPONENT_ADDED, encodeEvent(apiEvent2))
                }

                val api = harness.api
                val addEvvaComponentEventPair =
                    api.addEvvaComponentAsync(
                        UUID.fromString("2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be"),
                        ComponentType.Cylinder,
                    )
                val test = addEvvaComponentEventPair.installationPointChangedDeferred.await()
                test.aggregateId.shouldBeEqual(
                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
                )
                val evvaComponentAdded =
                    addEvvaComponentEventPair.evvaComponentAddedDeferred.await()
                evvaComponentAdded.type.shouldBe(ComponentType.Cylinder)

                harness
                    .publishedPayload(Topics.Command.ADD_EVVA_COMPONENT)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"id\":\"2d52bd95-18ba-4e46-8f00-0fc4c1e3f9be\",\"type\":\"Cylinder\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
