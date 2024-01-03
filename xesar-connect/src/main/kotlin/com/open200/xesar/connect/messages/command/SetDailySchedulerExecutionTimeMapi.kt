package com.open200.xesar.connect.messages.command

import com.open200.xesar.connect.utils.LocalTimeSerializer
import com.open200.xesar.connect.utils.UUIDSerializer
import java.time.LocalTime
import java.util.*
import kotlinx.serialization.Serializable

/**
 * Represents a command POJO to set the daily scheduler execution time.
 *
 * @param dailySchedulerExecutionTime The time of day when the daily scheduler should be executed.
 * @param commandId The id of the command.
 * @param token The token of the command.
 */
@Serializable
data class SetDailySchedulerExecutionTimeMapi(
    @Serializable(with = LocalTimeSerializer::class) val dailySchedulerExecutionTime: LocalTime,
    @Serializable(with = UUIDSerializer::class) val commandId: UUID,
    val token: String
) : Command
