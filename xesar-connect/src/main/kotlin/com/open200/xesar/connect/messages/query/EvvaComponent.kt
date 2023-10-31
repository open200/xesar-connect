package com.open200.xesar.connect.messages.query

import QueryElementResource
import QueryListResource
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class EvvaComponent(
    val componentType: ComponentType? = null,
    val batteryCondition: BatteryCondition? = null,
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    @Serializable(with = LocalDateTimeSerializer::class)
    val batteryStatusUpdatedAt: LocalDateTime? = null,
    @Serializable(with = LocalDateTimeSerializer::class) val stateChangedAt: LocalDateTime? = null,
    val status: OnlineStatus? = null
) : QueryListResource, QueryElementResource {

    companion object {
        const val QUERY_RESOURCE = "evva-components"
    }
}
