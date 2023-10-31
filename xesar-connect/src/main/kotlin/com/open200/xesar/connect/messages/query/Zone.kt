package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Zone(
    val installationPoint: List<InstallationPoint>? = null,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    val installationPointCount: Int? = null,
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "zones"
    }
}
