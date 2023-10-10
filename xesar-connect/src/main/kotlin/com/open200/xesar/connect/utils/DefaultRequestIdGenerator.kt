package com.open200.xesar.connect.utils

import java.util.UUID

/** Default implementation of the request ID generator. */
class DefaultRequestIdGenerator : IRequestIdGenerator {
    /**
     * Generates a new random UUID as the request ID.
     *
     * @return The generated request ID.
     */
    override fun generateId(): UUID {
        return UUID.randomUUID()
    }
}
