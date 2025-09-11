package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.utils.OffsetDateTimeSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.lang.IllegalArgumentException
import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows

class OffsetDateTimeSerializerTest :
    FunSpec({
        val offsetDateTimeSerializer = OffsetDateTimeSerializer

        val dateStringWithMilliSecond = "2025-08-28T21:59:59.999999999+02:00"
        val offsetDateTimeWithMilliSecond = OffsetDateTime.parse(dateStringWithMilliSecond)

        test("serialize OffsetDateTime with milliseconds and offset should return correct String") {
            val offsetDateTimeEncoded =
                Json.encodeToString(offsetDateTimeSerializer, offsetDateTimeWithMilliSecond)

            offsetDateTimeEncoded shouldBeEqual "\"$dateStringWithMilliSecond\""
        }

        test("deserialize OffsetDateTime with milliseconds and offset should return correct OffsetDateTime") {
            val localDateTimeDecoded =
                Json.decodeFromString(offsetDateTimeSerializer, "\"$dateStringWithMilliSecond\"")

            localDateTimeDecoded shouldBeEqual offsetDateTimeWithMilliSecond
        }

        test("serialize OffsetDateTime should throw an exception") {
            assertThrows<DateTimeParseException> {
                Json.encodeToString(offsetDateTimeSerializer, OffsetDateTime.parse("invalid"))
            }
        }

        test("deserialize OffsetDateTime should throw an exception") {
            assertThrows<IllegalArgumentException>() {
                Json.decodeFromString(offsetDateTimeSerializer, "invalid")
            }
        }

        val dateTimeString2 = "2025-08-28T11:20:00+02:00"
        val offsetDateTime2 = OffsetDateTime.parse(dateTimeString2)

        test("serialize OffsetDateTime with offset but without milliseconds should return correct String") {
            val offsetDateTimeEncoded = Json.encodeToString(offsetDateTimeSerializer, offsetDateTime2)

            offsetDateTimeEncoded shouldBeEqual "\"$dateTimeString2\""
        }

        test(
            "deserialize OffsetDateTime with offset but without milliseconds should return correct OffsetDateTime"
        ) {
            val offsetDateTimeDecoded =
                Json.decodeFromString(offsetDateTimeSerializer, "\"$dateTimeString2\"")

            offsetDateTimeDecoded shouldBeEqual offsetDateTime2
        }

        val dateTimeString3 = "2025-09-11T06:57:42.180755298Z"
        val offsetDateTime3 = OffsetDateTime.parse(dateTimeString3)

        test("serialize OffsetDateTime in UTC format should return correct String") {
            val offsetDateTimeEncoded = Json.encodeToString(offsetDateTimeSerializer, offsetDateTime3)

            offsetDateTimeEncoded shouldBeEqual "\"$dateTimeString3\""
        }

        test(
            "deserialize OffsetDateTime in UTC format should return correct OffsetDateTime"
        ) {
            val offsetDateTimeDecoded =
                Json.decodeFromString(offsetDateTimeSerializer, "\"$dateTimeString3\"")

            offsetDateTimeDecoded shouldBeEqual offsetDateTime3
        }
    })
