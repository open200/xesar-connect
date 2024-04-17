package com.open200.xesar.connect.messages.query

import com.open200.xesar.connect.messages.ComponentType
import com.open200.xesar.connect.utils.LocalDateTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalDateTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents an EVVA component in the system.
 *
 * @param componentType The type of the component.
 * @param batteryCondition The battery condition of the component (optional).
 * @param id The unique identifier of the component.
 * @param batteryStatusUpdatedAt The date and time the battery status was updated (optional).
 * @param stateChangedAt The date and time the state of the component was changed (optional).
 * @param status The online status of the component (optional).
 */
@Serializable
data class EvvaComponent(
    val componentType: ComponentType,
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
