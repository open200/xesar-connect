package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.EvvaComponentAdded
import com.open200.xesar.connect.messages.event.InstallationPointChanged
import java.util.Optional
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

/** Pair of events returning from [com.open200.xesar.connect.extension.addEvvaComponentAsync] */
data class AddEvvaComponentResult(
    val installationPointChangedDeferred: Deferred<InstallationPointChanged>,
    val evvaComponentAddedDeferred: Deferred<EvvaComponentAdded>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>,
) {
    suspend fun await() = awaitAll(installationPointChangedDeferred, evvaComponentAddedDeferred)
}
