package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.EntityType
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.SingleEventResult
import com.open200.xesar.connect.messages.command.AddEntityMetadataDefinitionMapi
import com.open200.xesar.connect.messages.command.DeleteEntityMetadataDefinitionMapi
import com.open200.xesar.connect.messages.command.RenameEntityMetadataDefinitionMapi
import com.open200.xesar.connect.messages.command.SetDailySchedulerExecutionTimeMapi
import com.open200.xesar.connect.messages.command.SetDefaultValidityDurationMapi
import com.open200.xesar.connect.messages.command.SetInstallationPointPersonalReferenceDurationMapi
import com.open200.xesar.connect.messages.command.SetPersonPersonalReferenceDurationMapi
import com.open200.xesar.connect.messages.command.SetReplacementMediumDurationMapi
import com.open200.xesar.connect.messages.command.SetValidityThresholdMapi
import com.open200.xesar.connect.messages.event.PartitionChanged
import java.time.LocalTime
import java.util.*

/**
 * Sets the daily scheduler execution time in the default partition asynchronously.
 *
 * @param dailySchedulerExecutionTime The time of day when the daily scheduler should be executed
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDailySchedulerExecutionTimeAsync(
    dailySchedulerExecutionTime: LocalTime,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetDailySchedulerExecutionTimeMapi, PartitionChanged>(
        Topics.Command.SET_DAILY_SCHEDULER_EXECUTION_TIME,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetDailySchedulerExecutionTimeMapi(
            config.uuidGenerator.generateId(),
            dailySchedulerExecutionTime,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the default validity duration in the default partition asynchronously.
 *
 * @param validityDuration The default validity duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setDefaultValidityDurationAsync(
    validityDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetDefaultValidityDurationMapi, PartitionChanged>(
        Topics.Command.SET_DEFAULT_VALIDITY_DURATION,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetDefaultValidityDurationMapi(config.uuidGenerator.generateId(), validityDuration, token),
        requestConfig,
    )
}

/**
 * Sets the default logging of personal data for installation point in the default partition
 * asynchronously.
 *
 * @param personalReferenceDuration The default personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setInstallationPointPersonalReferenceDurationAsync(
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetInstallationPointPersonalReferenceDurationMapi, PartitionChanged>(
        Topics.Command.SET_INSTALLATION_POINT_PERSONAL_REFERENCE_DURATION,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetInstallationPointPersonalReferenceDurationMapi(
            config.uuidGenerator.generateId(),
            personalReferenceDuration,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the default logging of personal data for person in the default partition asynchronously.
 *
 * @param personalReferenceDuration The default personal reference duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setPersonPersonalReferenceDurationAsync(
    personalReferenceDuration: PersonalLog,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetPersonPersonalReferenceDurationMapi, PartitionChanged>(
        Topics.Command.SET_PERSON_PERSONAL_REFERENCE_DURATION,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetPersonPersonalReferenceDurationMapi(
            config.uuidGenerator.generateId(),
            personalReferenceDuration,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the replacement medium duration in the default partition asynchronously.
 *
 * @param replacementMediumDuration The replacement medium duration
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setReplacementMediumDurationAsync(
    replacementMediumDuration: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetReplacementMediumDurationMapi, PartitionChanged>(
        Topics.Command.SET_REPLACEMENT_MEDIUM_DURATION,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetReplacementMediumDurationMapi(
            config.uuidGenerator.generateId(),
            replacementMediumDuration,
            token,
        ),
        requestConfig,
    )
}

/**
 * Sets the validity duration in the default partition asynchronously.
 *
 * @param validityThreshold The validity threshold
 * @param requestConfig The request configuration (optional).
 */
suspend fun XesarConnect.setValidityThresholdAsync(
    validityThreshold: Short,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<SetValidityThresholdMapi, PartitionChanged>(
        Topics.Command.SET_VALIDITY_THRESHOLD,
        Topics.Event.PARTITION_CHANGED,
        true,
        SetValidityThresholdMapi(config.uuidGenerator.generateId(), validityThreshold, token),
        requestConfig,
    )
}

/**
 * Adds one or more metadata definitions to a Xesar entity of the default partition. If any metadata
 * definition with the same name already exists it will be ignored.
 *
 * @param entityType The target entity type to add the metadata definitions to.
 * @param names The names of the metadata definitions to add. Each name should be unique.
 */
suspend fun XesarConnect.addEntityMetadataDefinitionAsync(
    entityType: EntityType,
    vararg names: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<AddEntityMetadataDefinitionMapi, PartitionChanged>(
        Topics.Command.ADD_ENTITY_METADATA_DEFINITION,
        Topics.Event.PARTITION_CHANGED,
        true,
        AddEntityMetadataDefinitionMapi(
            config.uuidGenerator.generateId(),
            entityType,
            names = names.toList(),
            token,
        ),
        requestConfig,
    )
}

/**
 * Removes one or more metadata definitions from a Xesar entity on the default partition. If any
 * provided definition name doesn't exist it will be ignored.
 *
 * @param entityType The target entity type to remove the metadata definitions.
 * @param names The names of the metadata definitions to remove. Each name should be unique.
 */
suspend fun XesarConnect.deleteEntityMetadataDefinitionAsync(
    entityType: EntityType,
    vararg names: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<DeleteEntityMetadataDefinitionMapi, PartitionChanged>(
        Topics.Command.DELETE_ENTITY_METADATA_DEFINITION,
        Topics.Event.PARTITION_CHANGED,
        true,
        DeleteEntityMetadataDefinitionMapi(
            config.uuidGenerator.generateId(),
            entityType,
            names = names.toList(),
            token,
        ),
        requestConfig,
    )
}

/**
 * Renames a specific metadata definition of a Xesar entity. Will fail if the new name is already in
 * use or if no metadata exists with the old name.
 *
 * @param entityType The target entity type to rename the metadata definition of.
 * @param metadataDefinitionId The ID of the metadata definition to rename.
 * @param name The new name to assign to the definition.
 */
suspend fun XesarConnect.renameEntityMetadataDefinitionAsync(
    entityType: EntityType,
    metadataDefinitionId: String,
    name: String,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig(),
): SingleEventResult<PartitionChanged> {
    return sendCommandAsync<RenameEntityMetadataDefinitionMapi, PartitionChanged>(
        Topics.Command.RENAME_ENTITY_METADATA_DEFINITION,
        Topics.Event.PARTITION_CHANGED,
        true,
        RenameEntityMetadataDefinitionMapi(
            config.uuidGenerator.generateId(),
            entityType,
            UUID.fromString(metadataDefinitionId),
            name,
            token,
        ),
        requestConfig,
    )
}
