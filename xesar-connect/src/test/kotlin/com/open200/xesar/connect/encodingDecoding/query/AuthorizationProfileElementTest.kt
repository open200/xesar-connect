package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.AuthorizationProfile
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.testutils.AuthorizationProfileFixture.authorizationProfileFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class AuthorizationProfileElementTest :
    FunSpec({
        val authorizationProfileTest =
            QueryElement(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                authorizationProfileFixture)

        val authorizationProfileString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                "\"response\":" +
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
                "\"standardTimeProfile\":\"a58e45f8-7bff-4b3a-bd0e-a831b3fa8053\"}}"

        test("encoding QueryListElement for an authorization profile") {
            val authorizationProfileEncoded = encodeQueryElement(authorizationProfileTest)

            authorizationProfileEncoded.shouldBeEqual(authorizationProfileString)
        }

        test("decoding QueryListElement for an authorization profile") {
            val authorizationProfileDecoded =
                decodeQueryElement<AuthorizationProfile>(authorizationProfileString)
            authorizationProfileDecoded.shouldBe(authorizationProfileTest)
        }
    })
