package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.utils.LocalTimeSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows
import java.time.LocalTime
import java.time.format.DateTimeParseException

class LocalTimeSerializerTest :
    FunSpec({
        val localTimeSerializer = LocalTimeSerializer
        val timeString = "23:59"
        val localTime = LocalTime.parse(timeString)

        test("serialize LocalTime should return correct String") {
            val localDateEncoded = Json.encodeToString(localTimeSerializer, localTime)
            localDateEncoded.shouldBeEqual("\"$timeString\"")
        }

        test("deserialize LocalTime should return correct LocalTime") {
            val localDateTimeDecoded = Json.decodeFromString(localTimeSerializer, "\"$timeString\"")
            localDateTimeDecoded.shouldBeEqual(localTime)
        }

        test("serialize LocalTime should throw an exception") {
            assertThrows<DateTimeParseException> {
                Json.encodeToString(localTimeSerializer, LocalTime.parse("wrong"))
            }
        }
    })
