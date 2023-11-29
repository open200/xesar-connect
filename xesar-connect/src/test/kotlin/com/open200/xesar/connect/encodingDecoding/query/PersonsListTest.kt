package com.open200.xesar.connect.encodingDecoding.query

import com.open200.xesar.connect.exception.ParsingException
import com.open200.xesar.connect.messages.query.Person
import com.open200.xesar.connect.messages.query.QueryList
import com.open200.xesar.connect.messages.query.decodeQueryList
import com.open200.xesar.connect.messages.query.encodeQueryList
import com.open200.xesar.connect.testutils.PersonFixture.personFixture
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.util.*

class PersonsListTest :
    FunSpec({
        val personsList =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        personFixture,
                        personFixture.copy(
                            id = UUID.fromString("cf45d8d5-4f99-4d85-a9b3-204c2ed3c56a"),
                            firstName = "firstname 2 String",
                            lastName = "lastname 2 String",
                            externalId = "1234",
                        ),
                    ),
                    2,
                    2,
                ))

        val personsString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                "\"response\"" +
                ":{\"data\":[{\"id\":\"0d92657f-a5dd-489b-a788-4306815c5641\"," +
                "\"partitionId\":\"7ae501f7-173e-43e5-842c-662b44720734\"," +
                "\"firstName\":\"firstname String\"," +
                "\"lastName\":\"lastname String\"," +
                "\"identifier\":\"1234\"," +
                "\"personalReferenceDuration\":{\"logMode\":\"log String\",\"days\":0}," +
                "\"disengagePeriod\":\"SHORT\"," +
                "\"identificationMediaCount\":0," +
                "\"outdatedMedia\":true," +
                "\"externalId\":\"123\"," +
                "\"external\":false}," +
                "{\"id\":\"cf45d8d5-4f99-4d85-a9b3-204c2ed3c56a\"," +
                "\"partitionId\":\"7ae501f7-173e-43e5-842c-662b44720734\"," +
                "\"firstName\":\"firstname 2 String\"," +
                "\"lastName\":\"lastname 2 String\"," +
                "\"identifier\":\"1234\"," +
                "\"personalReferenceDuration\":" +
                "{\"logMode\":\"log String\",\"days\":0}," +
                "\"disengagePeriod\":\"SHORT\"," +
                "\"identificationMediaCount\":0," +
                "\"outdatedMedia\":true," +
                "\"externalId\":\"1234\"," +
                "\"external\":false}]," +
                "\"totalCount\":2," +
                "\"filterCount\":2}}"

        test("encoding QueryListElement for a list of persons") {
            val personsEncoded = encodeQueryList(personsList)
            personsEncoded.shouldBeEqual(personsString)
        }

        test("decoding QueryListElement for a list of persons") {
            val personsDecoded = decodeQueryList<Person>(personsString)
            personsDecoded.shouldBe(personsList)
        }

        test("decoding QueryResponseList for a person with additional properties") {
            val personsListWithAdditionalProperties =
                "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                    "\"response\"" +
                    ":{\"data\":[{\"id\":\"0d92657f-a5dd-489b-a788-4306815c5641\"," +
                    "\"partitionId\":\"7ae501f7-173e-43e5-842c-662b44720734\"," +
                    "\"firstName\":\"firstname String\"," +
                    "\"lastName\":\"lastname String\"," +
                    "\"identifier\":\"1234\"," +
                    "\"personalReferenceDuration\":{\"logMode\":\"log String\",\"days\":0}," +
                    "\"disengagePeriod\":\"SHORT\"," +
                    "\"identificationMediaCount\":0," +
                    "\"outdatedMedia\":true," +
                    "\"externalId\":\"123\"," +
                    "\"external\":false}," +
                    "{\"id\":\"cf45d8d5-4f99-4d85-a9b3-204c2ed3c56a\"," +
                    "\"partitionId\":\"7ae501f7-173e-43e5-842c-662b44720734\"," +
                    "\"firstName\":\"firstname 2 String\"," +
                    "\"lastName\":\"lastname 2 String\"," +
                    "\"identifier\":\"1234\"," +
                    "\"additionalProperties\":\"additional Information\"," +
                    "\"personalReferenceDuration\":" +
                    "{\"logMode\":\"log String\",\"days\":0}," +
                    "\"disengagePeriod\":\"SHORT\"," +
                    "\"identificationMediaCount\":0," +
                    "\"outdatedMedia\":true," +
                    "\"externalId\":\"1234\"," +
                    "\"external\":false}]," +
                    "\"totalCount\":2," +
                    "\"additionalProperties\":\"additional Information\"," +
                    "\"filterCount\":2}}"

            val personsDecoded = decodeQueryList<Person>(personsListWithAdditionalProperties)

            personsDecoded.shouldBeEqual(personsList)
        }

        test("decoding QueryResponseElement for a person should throw a Parsing exception") {
            shouldThrow<ParsingException> { decodeQueryList<Person>("personString") }
        }
    })
