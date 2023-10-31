package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalDateSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDate
import java.util.*

@Serializable
data class Calendar(
    val serialVersionUID: Long? = null,
    @Serializable(with = UUIDSerializer::class) val partitionId: UUID? = null,
    val name: String? = null,
    val specialDays: List<@Serializable(with = LocalDateSerializer::class) LocalDate>? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "calendars"
    }
}
