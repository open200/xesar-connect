package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.MediumAuthorizationProfileChanged
import com.open200.xesar.connect.messages.event.MediumChanged
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

/**
 * Pair of events returning from
 * [com.open200.xesar.connect.extension.assignAuthorizationProfileToMediumAsync]
 */
class AssignAuthorizationProfileToMediumResult(
    val mediumChangedDeferred: Deferred<MediumChanged>,
    val mediumAuthorizationProfileChangedDeferred: Deferred<MediumAuthorizationProfileChanged>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>,
) {
    suspend fun await() = awaitAll(mediumChangedDeferred, mediumAuthorizationProfileChangedDeferred)
}
