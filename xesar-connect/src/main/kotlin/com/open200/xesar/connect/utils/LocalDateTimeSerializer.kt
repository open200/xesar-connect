package com.open200.xesar.connect.utils

import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Serializer for LocalDateTime objects. */
object LocalDateTimeSerializer : KSerializer<LocalDateTime> {

    /** The descriptor for LocalDateTime. */
    override val descriptor = PrimitiveSerialDescriptor("LocalDateTime", PrimitiveKind.STRING)

    /**
     * Serializes the LocalDateTime object into a string representation.
     *
     * @param encoder The encoder used for serialization.
     * @param value The LocalDateTime object to serialize.
     */
    override fun serialize(encoder: Encoder, value: LocalDateTime) =
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))

    /**
     * Deserializes the string representation into a LocalDateTime object.
     *
     * @param decoder The decoder used for deserialization.
     * @return The deserialized LocalDateTime object.
     */
    override fun deserialize(decoder: Decoder): LocalDateTime {
        val input = decoder.decodeString()

        return try {
            // First try plain local (no offset)
            LocalDateTime.parse(input, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        } catch (e: Exception) {
            // If there's an offset or 'Z', parse as OffsetDateTime and drop the offset
            OffsetDateTime.parse(input, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDateTime()
        }
    }
}
