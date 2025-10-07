package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.util.fixture.IdentificationMediumFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class IdentificationMediumListTest :
    FunSpec({
        val identificationMediumList =
            QueryList(
                UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"),
                QueryList.Response(
                    listOf(
                        IdentificationMediumFixture.identificationMediumFixture,
                        IdentificationMediumFixture.identificationMediumFixture.copy(
                            id = UUID.fromString("8293e920-90ce-48da-851c-cff54a13e2c6")
                        ),
                    ),
                    2,
                    2,
                ),
            )

        val identificationMediumString =
            "{\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\",\"response\":{\"data\":[{\"id\":\"8293e920-90ce-48da-851c-cff54a13e2c6\",\"label\":\"test door\",\"issuedAt\":\"2023-07-05T15:22:13.509825\",\"syncedAt\":\"2023-07-05T15:22:38.230076\",\"validityDuration\":151,\"authorizationProfileId\":\"d08fdd62-bc36-4e47-8bc9-62b603e75ed9\",\"authorizationProfileName\":\"Authorization Profile 1\",\"individualAuthorizationProfileIds\":[\"3dba6935-6904-4bc0-99d3-8115c9bbbedc\",\"66b9f5d9-5664-4bb9-9546-af315987752b\"],\"mediumState\":\"ACTIVE\",\"accessBeginAt\":\"2021-01-01T00:00:00\",\"accessEndAt\":\"2021-01-01T00:00:00\",\"validityBeginAt\":\"2021-01-01T00:00:00\",\"validityEndAt\":\"2021-01-01T00:00:00\",\"validityBeginAtInHardware\":\"2021-01-01T00:00:00\",\"validityEndAtInHardware\":\"2021-01-01T00:00:00\",\"external\":false,\"disengagePeriod\":\"SHORT\",\"mediumIdentifier\":1,\"outdated\":true,\"personId\":\"82a8a2cc-5d39-4cfa-b04e-49111a0bcdf7\",\"person\":\"test Person\",\"hardwareId\":\"f45ebc226706800e1cf942c995d0a43db78ec563173fb79a43eedbfbd8f29222\",\"nativeId\":\"046e0b8a967280\",\"secure\":true,\"softwareStatus\":\"ACTIVE\",\"hardwareStatus\":\"ACTIVE\",\"fitsOnHardware\":true,\"userId\":\"91781b22-ebdf-4ada-80cf-91f1fb9a4d96\",\"userName\":\"test User\",\"requiredAction\":\"UPDATE\",\"mediumType\":\"PASSIVE\",\"phoneNumber\":null,\"registrationState\":null,\"registrationCode\":null,\"registrationCodeValidUntil\":null,\"messageLanguage\":null,\"entityMetadata\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"type\",\"value\":\"card\"},{\"id\":\"0f8fad5b-d9cb-469f-a165-70867728950e\",\"name\":\"amount\",\"value\":null}]},{\"id\":\"8293e920-90ce-48da-851c-cff54a13e2c6\",\"label\":\"test door\",\"issuedAt\":\"2023-07-05T15:22:13.509825\",\"syncedAt\":\"2023-07-05T15:22:38.230076\",\"validityDuration\":151,\"authorizationProfileId\":\"d08fdd62-bc36-4e47-8bc9-62b603e75ed9\",\"authorizationProfileName\":\"Authorization Profile 1\",\"individualAuthorizationProfileIds\":[\"3dba6935-6904-4bc0-99d3-8115c9bbbedc\",\"66b9f5d9-5664-4bb9-9546-af315987752b\"],\"mediumState\":\"ACTIVE\",\"accessBeginAt\":\"2021-01-01T00:00:00\",\"accessEndAt\":\"2021-01-01T00:00:00\",\"validityBeginAt\":\"2021-01-01T00:00:00\",\"validityEndAt\":\"2021-01-01T00:00:00\",\"validityBeginAtInHardware\":\"2021-01-01T00:00:00\",\"validityEndAtInHardware\":\"2021-01-01T00:00:00\",\"external\":false,\"disengagePeriod\":\"SHORT\",\"mediumIdentifier\":1,\"outdated\":true,\"personId\":\"82a8a2cc-5d39-4cfa-b04e-49111a0bcdf7\",\"person\":\"test Person\",\"hardwareId\":\"f45ebc226706800e1cf942c995d0a43db78ec563173fb79a43eedbfbd8f29222\",\"nativeId\":\"046e0b8a967280\",\"secure\":true,\"softwareStatus\":\"ACTIVE\",\"hardwareStatus\":\"ACTIVE\",\"fitsOnHardware\":true,\"userId\":\"91781b22-ebdf-4ada-80cf-91f1fb9a4d96\",\"userName\":\"test User\",\"requiredAction\":\"UPDATE\",\"mediumType\":\"PASSIVE\",\"phoneNumber\":null,\"registrationState\":null,\"registrationCode\":null,\"registrationCodeValidUntil\":null,\"messageLanguage\":null,\"entityMetadata\":[{\"id\":\"123e4567-e89b-12d3-a456-426614174000\",\"name\":\"type\",\"value\":\"card\"},{\"id\":\"0f8fad5b-d9cb-469f-a165-70867728950e\",\"name\":\"amount\",\"value\":null}]}],\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryResponseElement for an identification medium") {
            val identificationMediumEncoded = encodeQueryList(identificationMediumList)
            identificationMediumEncoded.shouldBeEqual(identificationMediumString)
        }

        test("decoding QueryResponseElement for an identification medium") {
            val identificationMediumsDecoded =
                decodeQueryList<IdentificationMedium>(identificationMediumString)

            identificationMediumsDecoded.shouldBeEqual(identificationMediumList)
        }
    })
