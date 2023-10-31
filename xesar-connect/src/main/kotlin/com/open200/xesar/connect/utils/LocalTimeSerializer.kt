package com.open200.xesar.connect.utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor = PrimitiveSerialDescriptor("LocalTime", PrimitiveKind.STRING)

    private val localTimeFormatter = DateTimeFormatter.ofPattern("H:m")

    override fun serialize(encoder: Encoder, value: LocalTime) =
        encoder.encodeString(value.format(localTimeFormatter))

    override fun deserialize(decoder: Decoder): LocalTime =
        LocalTime.parse(decoder.decodeString(), DateTimeFormatter.ISO_LOCAL_TIME)
}
