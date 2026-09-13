package com.open200.xesar.connect.command

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.extension.createPersonAsync
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.event.ApiEvent
import com.open200.xesar.connect.messages.event.PersonCreated
import com.open200.xesar.connect.messages.event.encodeEvent
import com.open200.xesar.connect.util.MockedXesarConnect
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*
import kotlinx.coroutines.test.runTest

class CreatePersonTest :
    FunSpec({
        test("create person") {
            runTest {
                val harness = MockedXesarConnect(this)
                harness.respondTo(Topics.Command.CREATE_PERSON) {
                    val apiEvent =
                        ApiEvent(
                            UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                            PersonCreated(
                                id = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                                firstName = "firstName",
                                lastName = "lastName",
                                partitionId =
                                    UUID.fromString("5c4d62bb-cdf9-4630-83ff-816ee8166d62"),
                                personalReferenceDuration = PersonalLog(days = 30),
                            ),
                        )

                    emitMessage(Topics.Event.PERSON_CREATED, encodeEvent(apiEvent))
                }

                val api = harness.api
                val result =
                    api.createPersonAsync(
                            firstName = "firstName",
                            lastName = "lastName",
                            personId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd"),
                        )
                        .await()
                result.firstName.shouldBeEqual("firstName")
                result.lastName.shouldBeEqual("lastName")

                harness
                    .publishedPayload(Topics.Command.CREATE_PERSON)
                    .shouldBeEqual(
                        "{\"commandId\":\"00000000-1281-40ae-89d7-5c541d77a757\",\"firstName\":\"firstName\",\"lastName\":\"lastName\",\"identifier\":null,\"externalId\":null,\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\",\"entityMetadata\":null,\"token\":\"JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx\"}"
                    )
            }
        }
    })
