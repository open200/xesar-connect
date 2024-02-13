package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.AddMediumToInstallationRequested
import com.open200.xesar.connect.messages.event.MediumAddedToInstallation
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

/**
 * Pair of events returning from
 * [com.open200.xesar.connect.extension.requestToAddMediumToInstallationAsync]
 */
class RequestAddMediumToInstallationResult(
    val addMediumToInstallationRequestedDeferred: Deferred<AddMediumToInstallationRequested>,
    val mediumAddedToInstallationDeferred: Deferred<MediumAddedToInstallation>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>
) {
    suspend fun await() =
        awaitAll(addMediumToInstallationRequestedDeferred, mediumAddedToInstallationDeferred)
}
