package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.lang.IllegalArgumentException
import java.time.LocalDateTime
import java.time.format.DateTimeParseException
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows

class LocalDateTimeSerializerTest :
    FunSpec({
        val localDateTimeSerializer = LocalDateTimeSerializer

        val dateStringWithMilliSecond = "2023-07-05T15:22:13.509"

        val localDateTimeWithMilliSecond = LocalDateTime.parse(dateStringWithMilliSecond)

        test("serialize LocalDateTime with milliseconds should return correct String") {
            val localDateTimeEncoded =
                Json.encodeToString(localDateTimeSerializer, localDateTimeWithMilliSecond)

            localDateTimeEncoded shouldBeEqual "\"$dateStringWithMilliSecond\""
        }

        test("deserialize LocalDateTime with milliseconds should return correct LocalDateTime") {
            val localDateTimeDecoded =
                Json.decodeFromString(localDateTimeSerializer, "\"$dateStringWithMilliSecond\"")

            localDateTimeDecoded shouldBeEqual localDateTimeWithMilliSecond
        }

        test("serialize LocalDateTime should throw an exception") {
            assertThrows<DateTimeParseException> {
                Json.encodeToString(localDateTimeSerializer, LocalDateTime.parse("invalid"))
            }
        }

        test("deserialize LocalDateTime should throw an exception") {
            assertThrows<IllegalArgumentException>() {
                Json.decodeFromString(localDateTimeSerializer, "invalid")
            }
        }

        val dateString2 = "2023-07-05T15:20:00"
        val localDateTime2 = LocalDateTime.parse(dateString2)

        test("serialize LocalDateTime without milliseconds should return correct String") {
            val localDateTimeEncoded = Json.encodeToString(localDateTimeSerializer, localDateTime2)

            localDateTimeEncoded shouldBeEqual "\"$dateString2\""
        }

        test(
            "deserialize LocalDateTime without milliseconds should return correct LocalDateTime"
        ) {
            val localDateTimeDecoded =
                Json.decodeFromString(localDateTimeSerializer, "\"$dateString2\"")

            localDateTimeDecoded shouldBeEqual localDateTime2
        }
    })
