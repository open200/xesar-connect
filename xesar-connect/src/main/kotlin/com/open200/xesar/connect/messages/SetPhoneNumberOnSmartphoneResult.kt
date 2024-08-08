package com.open200.xesar.connect.messages

import com.open200.xesar.connect.messages.event.MediumChanged
import com.open200.xesar.connect.messages.event.PhoneNumberChanged
import java.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.awaitAll

/**
 * Pair of events returning from
 * [com.open200.xesar.connect.extension.setPhoneNumberOnSmartphoneAsync]
 */
class SetPhoneNumberOnSmartphoneResult(
    val mediumChangedDeferred: Deferred<MediumChanged>,
    val phoneNumberChangedDeferred: Deferred<PhoneNumberChanged>,
    val apiErrorDeferred: Deferred<Optional<ApiError>>
) {
    suspend fun await() = awaitAll(mediumChangedDeferred, phoneNumberChangedDeferred)
}
