package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class CodingStation(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val name: String? = null,
    val description: String? = null,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID,
    @Serializable(with = LocalDateTimeSerializer::class) val deleted: LocalDateTime? = null,
    val online: Boolean? = null
) : QueryListResource, QueryElementResource {
    companion object {
        const val QUERY_RESOURCE = "coding-stations"
    }
}
