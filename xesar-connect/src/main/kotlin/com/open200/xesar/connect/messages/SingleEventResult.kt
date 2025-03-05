package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.Event
import java.util.*
import kotlinx.coroutines.Deferred

/**
 * Result object of a command returning a single event. It includes a deferred object with an event
 * and a deferred object with a possible API error.
 *
 * @param eventDeferred will be completed with the event if no error occurs.
 * @param apiErrorDeferred will be completed with an empty Optional if no error occurs. Otherwise,
 *   it will contain an [ApiError].
 */
class SingleEventResult<out E : Event>(
    val eventDeferred: Deferred<E>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>,
) {
    suspend fun await() = eventDeferred.await()
}
