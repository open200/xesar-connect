package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.configureBluetoothStateAsync
import com.open200.xesar.connect.messages.BluetoothState
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*
import kotlinx.coroutines.test.runTest

class ConfigureBluetoothStateTest :
    FunSpec({
        test("configure bluetooth state of an installation point") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CONFIGURE_BLUETOOTH_STATE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                bluetoothState = BluetoothState.ENABLED,
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.configureBluetoothStateAsync(
                            UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            BluetoothState.ENABLED,
                        )
                        .await()
                result.bluetoothState.shouldBe(BluetoothState.ENABLED)

                harness
                    .publishedPayload(Topics.Command.CONFIGURE_BLUETOOTH_STATE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"bluetoothState\":\"ENABLED\",\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
