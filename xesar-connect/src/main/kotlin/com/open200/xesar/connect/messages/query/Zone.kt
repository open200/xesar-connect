package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.utils.UUIDSerializer
import java.util.*
import kotlinx.serialization.Serializable

@Serializable
data class Zone(
    val installationPoints: List<@Serializable(with = UUIDSerializer::class) UUID> = emptyList(),
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val installationPointCount: Int? = null,
    val name: String,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "zones"
    }
}
