package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.configureManualOfficeModeAndShopModeMapiAsync
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class ConfigureManualOfficeModeAndShopModeTest :
    FunSpec({
        test("configure assignable authorization profiles") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CONFIGURE_MANUAL_OFFICE_MODE_AND_SHOP_MODE) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            InstallationPointChanged(
                                shopMode = true,
                                aggregateId =
                                    UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                            ),
                        )

                    emitMessage(Topics.Event.INSTALLATION_POINT_CHANGED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.configureManualOfficeModeAndShopModeMapiAsync(
                            shopMode = true,
                            installationPointId =
                                UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.shopMode?.shouldBeTrue()

                harness
                    .publishedPayload(Topics.Command.CONFIGURE_MANUAL_OFFICE_MODE_AND_SHOP_MODE)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"shopMode\":true,\"manualOfficeMode\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
