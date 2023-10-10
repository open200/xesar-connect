package com.open200.xesar.connect.exception

/**
 * Base exception class for Xesar API-related exceptions.
 *
 * @param message The detail message (optional).
 * @param causeException The cause of the exception (optional).
 */
sealed class XesarApiException : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(causeException: Throwable) : super(causeException)

    constructor(message: String, causeException: Throwable) : super(message, causeException)
}
