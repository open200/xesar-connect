package com.open200.xesar.connect.utils

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Serializer for OffsetDateTime objects. */
object OffsetDateTimeSerializer : KSerializer<OffsetDateTime> {

    /** The descriptor for OffsetDateTime. */
    override val descriptor = PrimitiveSerialDescriptor("OffsetDateTime", PrimitiveKind.STRING)

    /**
     * Serializes the OffsetDateTime object into a string representation.
     *
     * @param encoder The encoder used for serialization.
     * @param value The OffsetDateTime object to serialize.
     */
    override fun serialize(encoder: Encoder, value: OffsetDateTime) =
        encoder.encodeString(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))

    /**
     * Deserializes the string representation into a OffsetDateTime object.
     *
     * @param decoder The decoder used for deserialization.
     * @return The deserialized OffsetDateTime object.
     */
    override fun deserialize(decoder: Decoder): OffsetDateTime =
        OffsetDateTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
}
