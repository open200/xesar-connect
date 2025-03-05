package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.*
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

/**
 * Pair of events returning from
 * [com.open200.xesar.connect.extension.changeAuthorizationProfileAsync]
 */
class ChangeAuthorizationProfileResult(
    val authorizationProfileInfoChangedDeferred: Deferred<AuthorizationProfileInfoChanged>,
    val authorizationProfileAccessChangedDeferred: Deferred<AuthorizationProfileAccessChanged>,
    val authorizationProfileChangedDeferred: Deferred<AuthorizationProfileChanged>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>,
) {
    suspend fun await() =
        awaitAll(
            authorizationProfileInfoChangedDeferred,
            authorizationProfileAccessChangedDeferred,
            authorizationProfileChangedDeferred,
        )
}
