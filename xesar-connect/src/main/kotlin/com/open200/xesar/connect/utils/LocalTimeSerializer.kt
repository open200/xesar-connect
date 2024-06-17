package com.open200.xesar.connect.utils

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: LocalTime) =
        encoder.encodeString(value.format(DateTimeFormatter.ISO_LOCAL_TIME))

    override fun deserialize(decoder: Decoder): LocalTime =
        LocalTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_LOCAL_TIME)
}
