package com.open200.xesar.connect.encodingDecoding.event

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.event.*
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class MetadataDefinitionsUpdatedSerializationTest :
    FunSpec({
        val entityId = UUID.fromString("43edc7cf-80ab-4486-86db-41cda2c7a2cd")
        val commandId = UUID.fromString("faf3d0c4-1281-40ae-89d7-5c541d77a757")
        val metadataId = UUID.fromString("a4b319e0-1281-40ae-89d7-5c541d77a757")

        val entityMetadata =
            listOf(EntityMetadata(id = metadataId, name = "testMetadata", value = "testValue"))

        val eventJsonSuffix =
            "\"event\":{\"entityMetadata\":[{\"id\":" +
                "\"a4b319e0-1281-40ae-89d7-5c541d77a757\"," +
                "\"name\":\"testMetadata\",\"value\":\"testValue\"}]," +
                "\"id\":\"43edc7cf-80ab-4486-86db-41cda2c7a2cd\"}}"

        val fullJson = "{\"commandId\":\"faf3d0c4-1281-40ae-89d7-5c541d77a757\",$eventJsonSuffix"

        test("serialize AuthorizationProfileMetadataDefinitionsUpdated") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    AuthorizationProfileMetadataDefinitionsUpdated(
                        entityMetadata = entityMetadata,
                        id = entityId,
                    ),
                )

            encodeEvent(apiEvent).shouldBeEqual(fullJson)
        }

        test("deserialize AuthorizationProfileMetadataDefinitionsUpdated") {
            decodeEvent<AuthorizationProfileMetadataDefinitionsUpdated>(fullJson)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        AuthorizationProfileMetadataDefinitionsUpdated(
                            entityMetadata = entityMetadata,
                            id = entityId,
                        ),
                    )
                )
        }

        test("serialize InstallationPointMetadataDefinitionsUpdated") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    InstallationPointMetadataDefinitionsUpdated(
                        entityMetadata = entityMetadata,
                        id = entityId,
                    ),
                )

            encodeEvent(apiEvent).shouldBeEqual(fullJson)
        }

        test("deserialize InstallationPointMetadataDefinitionsUpdated") {
            decodeEvent<InstallationPointMetadataDefinitionsUpdated>(fullJson)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        InstallationPointMetadataDefinitionsUpdated(
                            entityMetadata = entityMetadata,
                            id = entityId,
                        ),
                    )
                )
        }

        test("serialize MediumMetadataDefinitionsUpdated") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    MediumMetadataDefinitionsUpdated(entityMetadata = entityMetadata, id = entityId),
                )

            encodeEvent(apiEvent).shouldBeEqual(fullJson)
        }

        test("deserialize MediumMetadataDefinitionsUpdated") {
            decodeEvent<MediumMetadataDefinitionsUpdated>(fullJson)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        MediumMetadataDefinitionsUpdated(
                            entityMetadata = entityMetadata,
                            id = entityId,
                        ),
                    )
                )
        }

        test("serialize PersonMetadataDefinitionsUpdated") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    PersonMetadataDefinitionsUpdated(entityMetadata = entityMetadata, id = entityId),
                )

            encodeEvent(apiEvent).shouldBeEqual(fullJson)
        }

        test("deserialize PersonMetadataDefinitionsUpdated") {
            decodeEvent<PersonMetadataDefinitionsUpdated>(fullJson)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        PersonMetadataDefinitionsUpdated(
                            entityMetadata = entityMetadata,
                            id = entityId,
                        ),
                    )
                )
        }

        test("serialize ZoneMetadataDefinitionsUpdated") {
            val apiEvent =
                ApiEvent(
                    commandId,
                    ZoneMetadataDefinitionsUpdated(entityMetadata = entityMetadata, id = entityId),
                )

            encodeEvent(apiEvent).shouldBeEqual(fullJson)
        }

        test("deserialize ZoneMetadataDefinitionsUpdated") {
            decodeEvent<ZoneMetadataDefinitionsUpdated>(fullJson)
                .shouldBeEqual(
                    ApiEvent(
                        commandId,
                        ZoneMetadataDefinitionsUpdated(
                            entityMetadata = entityMetadata,
                            id = entityId,
                        ),
                    )
                )
        }
    })
