package com.open200.xesar.connect.utils

import com.open200.xesar.connect.messages.command.FilterType
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object FilterTypeSerializer : KSerializer<FilterType> {

    override val descriptor = PrimitiveSerialDescriptor("FilterType", PrimitiveKind.STRING)

    /**
     * Deserializes the value from the FilterType enum.
     *
     * @param decoder The decoder used for deserialization.
     * @return The deserialized FilterType object.
     */
    override fun deserialize(decoder: Decoder): FilterType {
        val filterString = decoder.decodeString()
        return FilterType.values().firstOrNull { it.filterString == filterString }
            ?: throw IllegalArgumentException("Unknown FilterType value: $filterString")
    }

    /**
     * Serializes the value from the FilterType enum.
     *
     * @param encoder The encoder used for serialization.
     * @param value The FilterType ENUM object to serialize.
     */
    override fun serialize(encoder: Encoder, value: FilterType) {
        encoder.encodeString(value.filterString ?: "")
    }
}
