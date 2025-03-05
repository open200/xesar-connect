package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.EvvaComponentAdded
import com.open200.xesar.connect.messages.event.InstallationPointCreated
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

/**
 * Pair of events returning from [com.open200.xesar.connect.extension.createInstallationPointAsync]
 */
class CreateInstallationPointResult(
    val installationPointCreatedDeferred: Deferred<InstallationPointCreated>,
    val evvaComponentAddedDeferred: Deferred<EvvaComponentAdded>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>,
) {
    suspend fun await() = awaitAll(installationPointCreatedDeferred, evvaComponentAddedDeferred)
}
