package com.open200.xesar.connect.utils

import java.util.*
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Serializer for UUID objects. */
object UUIDSerializer : KSerializer<UUID> {
    /** The descriptor for UUID. */
    override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    /**
     * Deserializes the string representation into a UUID object.
     *
     * @param decoder The decoder used for deserialization.
     * @return The deserialized UUID object.
     */
    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
    /**
     * Serializes the UUID object into a string representation.
     *
     * @param encoder The encoder used for serialization.
     * @param value The UUID object to serialize.
     */
    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }
}
