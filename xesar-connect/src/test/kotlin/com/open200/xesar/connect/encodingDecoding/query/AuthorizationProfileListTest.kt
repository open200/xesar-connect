package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.AuthorizationProfile
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.util.fixture.AuthorizationProfileFixture.authorizationProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class AuthorizationProfileListTest :
    FunSpec({
        val authorizationProfileTest =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        authorizationProfileFixture,
                        authorizationProfileFixture.copy(
                            id = UUID.fromString("555e7d1a-54f1-432a-ade7-80d20a63ee2d"),
                            name = "authorization profile 2 String",
                            description = "description profile 2 String",
                            installationPoints = listOf(),
                            zones = listOf(),
                        ),
                    ),
                    2,
                    2,
                ),
            )

        val authorizationProfileString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                "\"response\":{\"data\":[" +
                "{\"id\":\"0f0f5120-098c-4d8f-92f3-2a073b85ef8a\"," +
                "\"name\":\"authorization profile 1 String\"," +
                "\"description\":\"description profile 1 String\"," +
                "\"installationPoints\":[" +
                "{\"id\":\"d572db59-64f7-4853-b9f6-d3a218e1f4f4\"," +
                "\"timeProfileId\":\"a58e45f8-7bff-4b3a-bd0e-a831b3fa8053\"}]," +
                "\"zones\":[" +
                "{\"id\":\"dd51e0c8-29df-41e2-a68b-0697a965744d\"," +
                "\"timeProfileId\":\"532534ef-d5aa-4cca-acfb-e558c623b00a\"}]," +
                "\"manualOfficeMode\":true," +
                "\"anyAuthorizations\":true," +
                "\"standardTimeProfile\":\"a58e45f8-7bff-4b3a-bd0e-a831b3fa8053\"," +
                "\"entityMetadata\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"type\",\"value\":\"authorization type 1\"},{\"id\":\"0f8fad5b-d9cb-469f-a165-70867728950e\",\"name\":\"number\",\"value\":null}]}," +
                "{\"id\":\"555e7d1a-54f1-432a-ade7-80d20a63ee2d\"," +
                "\"name\":\"authorization profile 2 String\"," +
                "\"description\":\"description profile 2 String\"," +
                "\"installationPoints\":[]," +
                "\"zones\":[]," +
                "\"manualOfficeMode\":true," +
                "\"anyAuthorizations\":true," +
                "\"standardTimeProfile\":\"a58e45f8-7bff-4b3a-bd0e-a831b3fa8053\"," +
                "\"entityMetadata\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"type\",\"value\":\"authorization type 1\"},{\"id\":\"0f8fad5b-d9cb-469f-a165-70867728950e\",\"name\":\"number\",\"value\":null}]}]," +
                "\"totalCount\":2," +
                "\"filterCount\":2}}"

        test("encoding QueryListElement for a list of authorization profiles") {
            val authorizationProfileEncoded = encodeQueryList(authorizationProfileTest)
            authorizationProfileEncoded.shouldBeEqual(authorizationProfileString)
        }

        test("decoding QueryListElement for a list of authorization profiles") {
            val authorizationProfileDecoded =
                decodeQueryList<AuthorizationProfile>(authorizationProfileString)
            authorizationProfileDecoded.shouldBe(authorizationProfileTest)
        }
    })
