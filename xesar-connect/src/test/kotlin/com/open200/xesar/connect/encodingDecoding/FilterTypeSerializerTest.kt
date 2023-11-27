package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.messages.command.FilterType
import com.open200.xesar.connect.utils.FilterTypeSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows

class FilterTypeSerializerTest :
    FunSpec({
        val filterTypeSerializer = FilterTypeSerializer

        val filterType = FilterType.EQ

        val filterTypeString = "\"${filterType.filterString}\""

        test("serialize FilterType should return correct String") {
            val filterTypeEncoded = Json.encodeToString(filterTypeSerializer, filterType)

            filterTypeEncoded shouldBeEqual filterTypeString
        }

        test("deserialize FilterType should return correct FilterType") {
            val filterTypeDecoded = Json.decodeFromString(filterTypeSerializer, filterTypeString)

            filterTypeDecoded shouldBeEqual filterType
        }

        test("serialize FilterType should throw an exception") {
            assertThrows<IllegalArgumentException> {
                Json.encodeToString(filterTypeSerializer, FilterType.valueOf("filternotavailable"))
            }
        }

        test("deserialize FilterType should throw an exception") {
            assertThrows<IllegalArgumentException> {
                Json.decodeFromString(filterTypeSerializer, "filternotavailable")
            }
        }
    })
