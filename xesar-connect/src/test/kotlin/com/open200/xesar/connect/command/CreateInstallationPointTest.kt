package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createInstallationPointAsync
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

class CreateInstallationPointTest :
    FunSpec({
        test("add evva component returning both events") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_INSTALLATION_POINT) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointCreated(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
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

                    emitMessage(Topics.Event.INSTALLATION_POINT_CREATED, encodeEvent(apiEvent))

                    emitMessage(Topics.Event.EVVA_COMPONENT_ADDED, encodeEvent(apiEvent2))
                }

                val api = harness.api
                val createInstallationPointResult =
                    api.createInstallationPointAsync(
                        ComponentType.Cylinder,
                        UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                    )
                val test = createInstallationPointResult.installationPointCreatedDeferred.await()
                test.id.shouldBe(UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"))
                val evvaComponentAdded =
                    createInstallationPointResult.evvaComponentAddedDeferred.await()
                evvaComponentAdded.type.shouldBe(ComponentType.Cylinder)

                harness
                    .publishedPayload(Topics.Command.CREATE_INSTALLATION_POINT)
                    .shouldBeEqual(
                        "{\"componentType\":\"Cylinder\",\"aggregateId\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"linkedInstallationPoints\":{},\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
