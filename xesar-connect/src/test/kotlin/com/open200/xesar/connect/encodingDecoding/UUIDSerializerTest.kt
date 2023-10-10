package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.utils.UUIDSerializer
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.lang.IllegalArgumentException
import java.util.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.assertThrows

class UUIDSerializerTest :
    FunSpec({
        val uuidSerializer = UUIDSerializer

        val uuid = UUID.randomUUID()

        val uuidString = "\"$uuid\""

        test("serialize UUID should return correct String") {
            val uuidEncoded = Json.encodeToString(uuidSerializer, uuid)

            uuidEncoded shouldBeEqual uuidString
        }

        test("deserialize UUID should return correct UUID") {
            val uuidDecoded = Json.decodeFromString(uuidSerializer, uuidString)

            uuidDecoded shouldBeEqual uuid
        }

        test("serialize UUID should throw an exception") {
            assertThrows<IllegalArgumentException> {
                Json.encodeToString(uuidSerializer, UUID.fromString("invalid"))
            }
        }

        test("deserialize UUID should throw an exception") {
            assertThrows<IllegalArgumentException> {
                Json.decodeFromString(uuidSerializer, "invalid")
            }
        }
    })
