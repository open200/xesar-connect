package com.open200.xesar.connect.exception

/**
 * Exception indicating a failed connection to the Xesar API.
 *
 * @param message The detail message (optional).
 * @param causeException The cause of the exception (optional).
 */
class ConnectionFailedException : XesarApiException {
    constructor() : super()

    constructor(message: String) : super(message)

    constructor(causeException: Throwable) : super(causeException)

    constructor(message: String, causeException: Throwable) : super(message, causeException)
}
