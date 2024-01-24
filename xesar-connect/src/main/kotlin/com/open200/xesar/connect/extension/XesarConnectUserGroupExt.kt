package com.open200.xesar.connect.extension

import com.open200.xesar.connect.Topics
import com.open200.xesar.connect.XesarConnect
import com.open200.xesar.connect.messages.command.ConfigureAssignableAuthorizationProfilesMapi
import com.open200.xesar.connect.messages.event.UserGroupChanged
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Configures the assignable authorization profiles for a user group asynchronously.
 *
 * @param assignableAuthorizationProfiles The list of assignable authorization profiles.
 * @param userGroupId The id of the user group.
 * @param requestConfig The request configuration (optional).
 * @return A deferred object that resolves to a response containing the user group changed event.
 */
suspend fun XesarConnect.configureAssignableAuthorizationProfiles(
    assignableAuthorizationProfiles: List<UUID>,
    userGroupId: UUID,
    requestConfig: XesarConnect.RequestConfig = buildRequestConfig()
): Deferred<UserGroupChanged> {
    return sendCommand<ConfigureAssignableAuthorizationProfilesMapi, UserGroupChanged>(
        Topics.Command.CONFIGURE_ASSIGNABLE_AUTHORIZATION_PROFILES,
        ConfigureAssignableAuthorizationProfilesMapi(
            config.uuidGenerator.generateId(), assignableAuthorizationProfiles, userGroupId, token),
        requestConfig)
}
