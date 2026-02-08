package com.open200.xesar.connect.util.fixture

import com.open200.xesar.connect.messages.EntityMetadata
import com.open200.xesar.connect.messages.query.AuthorizationProfile
import java.util.*

object AuthorizationProfileFixture {

    val installationPointFixture =
        AuthorizationProfile.InstallationPoint(
            id = UUID.fromString("d572db59-64f7-4853-b9f6-d3a218e1f4f4"),
            timeProfileId = UUID.fromString("a58e45f8-7bff-4b3a-bd0e-a831b3fa8053"),
        )

    val zoneFixture =
        AuthorizationProfile.Zone(
            id = UUID.fromString("dd51e0c8-29df-41e2-a68b-0697a965744d"),
            timeProfileId = UUID.fromString("532534ef-d5aa-4cca-acfb-e558c623b00a"),
        )

    val authorizationProfileFixture =
        AuthorizationProfile(
            id = UUID.fromString("0f0f5120-098c-4d8f-92f3-2a073b85ef8a"),
            name = "authorization profile 1 String",
            description = "description profile 1 String",
            installationPoints = listOf(installationPointFixture),
            zones = listOf(zoneFixture),
            manualOfficeMode = true,
            anyAuthorizations = true,
            standardTimeProfile = UUID.fromString("a58e45f8-7bff-4b3a-bd0e-a831b3fa8053"),
            entityMetadata =
                listOf(
                    EntityMetadata(
                        id = UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                        name = "type",
                        value = "authorization type 1",
                    ),
                    EntityMetadata(
                        id = UUID.fromString("0f8fad5b-d9cb-469f-a165-70867728950e"),
                        name = "number",
                        value = null,
                    ),
                ),
        )
}
