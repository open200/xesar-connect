package com.open200.xesar.connect.messages

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.UUID
import kotlinx.serialization.Serializable

/**
 * Represents the entity metadata.
 *
 * @param id The ID of the metadata definition.
 * @param name The name of the metadata instance. All unicode characters allowed. Size between 1
 *   and 255.
 * @param value The value of the metadata. All unicode characters allowed. Size between 0 and 255
 *   (optional).
 */
@Serializable
data class EntityMetadata(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String,
    val value: String? = null,
)
