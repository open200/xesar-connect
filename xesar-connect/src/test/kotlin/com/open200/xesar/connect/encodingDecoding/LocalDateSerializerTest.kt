package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.utils.LocalDateSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.time.LocalDate
import java.time.format.DateTimeParseException
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows

class LocalDateSerializerTest :
    FunSpec({
        val localDateSerializer = LocalDateSerializer
        val dateString = "2018-02-25"
        val localDate = LocalDate.parse(dateString)

        test("serialize LocalDate should return correct String") {
            val localDateEncoded = Json.encodeToString(localDateSerializer, localDate)
            localDateEncoded.shouldBeEqual("\"$dateString\"")
        }

        test("deserialize LocalDate should return correct LocalDate") {
            val localDateTimeDecoded = Json.decodeFromString(localDateSerializer, "\"$dateString\"")
            localDateTimeDecoded.shouldBeEqual(localDate)
        }

        test("serialize LocalDate should throw an exception") {
            assertThrows<DateTimeParseException> {
                Json.encodeToString(localDateSerializer, LocalDate.parse("wrong"))
            }
        }
    })
