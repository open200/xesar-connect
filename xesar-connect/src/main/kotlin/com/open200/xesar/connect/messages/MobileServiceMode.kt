package com.open200.xesar.connect.messages

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/** Represents the two mobile service modes XMS and SELF_SERVICE. */
@Serializable
enum class MobileServiceMode {
    @SerialName("xms") XMS,
    @SerialName("self-service") SELF_SERVICE,
}
