package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.messages.query.Person
import com.open200.xesar.connect.messages.query.QueryElement
import com.open200.xesar.connect.messages.query.decodeQueryElement
import com.open200.xesar.connect.messages.query.encodeQueryElement
import com.open200.xesar.connect.util.fixture.PersonFixture.personFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.util.*

class PersonElementTest :
    FunSpec({
        val person =
            QueryElement(UUID.fromString("00000000-1281-42c0-9a15-c5844850c748"), personFixture)

        val personString =
            "{\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\"," +
                "\"response\":{" +
                "\"id\":\"0d92657f-a5dd-489b-a788-4306815c5641\"," +
                "\"partitionId\":\"7ae501f7-173e-43e5-842c-662b44720734\"," +
                "\"firstName\":\"firstname String\"," +
                "\"lastName\":\"lastname String\"," +
                "\"identifier\":\"1234\"," +
                "\"personalReferenceDuration\":{\"logMode\":\"dontSave\",\"days\":0}," +
                "\"disengagePeriod\":\"SHORT\"," +
                "\"identificationMediaCount\":0," +
                "\"outdatedMedia\":true," +
                "\"externalId\":\"123\"," +
                "\"external\":false,\"defaultAuthorizationProfile\":null,\"zones\":[],\"installationPoints\":[],\"defaultAuthorizationProfileId\":null," +
                "\"entityMetadata\":[]}}"

        test("encoding QueryResponseElement for a person") {
            val personEncoded = encodeQueryElement(person)

            personEncoded.shouldBeEqual(personString)
        }

        test("decoding QueryResponseElement for a person") {
            val personsDecoded = decodeQueryElement<Person>(personString)

            personsDecoded.shouldBeEqual(person)
        }

        test("decoding QueryResponseElement for a person with additional properties") {
            val personWithAdditionalProperties =
                "{\"requestId\":\"00000000-1281-42c0-9a15-c5844850c748\"," +
                    "\"response\":{" +
                    "\"id\":\"0d92657f-a5dd-489b-a788-4306815c5641\"," +
                    "\"partitionId\":\"7ae501f7-173e-43e5-842c-662b44720734\"," +
                    "\"firstName\":\"firstname String\"," +
                    "\"lastName\":\"lastname String\"," +
                    "\"identifier\":\"1234\"," +
                    "\"personalReferenceDuration\":{\"logMode\":\"dontSave\",\"days\":0}," +
                    "\"disengagePeriod\":\"SHORT\"," +
                    "\"identificationMediaCount\":0," +
                    "\"outdatedMedia\":true," +
                    "\"externalId\":\"123\"," +
                    "\"additionalProperties\":\"additional Information\"," +
                    "\"external\":false,\"defaultAuthorizationProfile\":null,\"zones\":[],\"installationPoints\":[],\"defaultAuthorizationProfileId\":null," +
                    "\"entityMetadata\":[]}}"

            val personsDecoded = decodeQueryElement<Person>(personWithAdditionalProperties)

            personsDecoded.shouldBeEqual(person)
        }

        test("decoding QueryResponseElement for a person should throw a Parsing exception") {
            shouldThrow<ParsingException> { decodeQueryElement<Person>("personString") }
        }
    })
