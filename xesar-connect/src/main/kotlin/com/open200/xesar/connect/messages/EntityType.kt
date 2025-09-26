package com.open200.xesar.connect.messages

import kotlinx.serialization.Serializable

/** Represents the type of the entity. */
@Serializable
enum class EntityType {
    PERSON,
    IDENTIFICATION_MEDIUM,
    ZONE,
    INSTALLATION_POINT,
    AUTHORIZATION_PROFILE,
}
