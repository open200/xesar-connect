package com.open200.xesar.connect.exception

/**
 * Exception indicating a failed login attempt in the Xesar API.
 *
 * @param message The detail message (optional).
 * @param causeException The cause of the exception (optional).
 */
class UnauthorizedLoginAttemptException : XesarApiException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(causeException: Throwable) : super(causeException)

    constructor(message: String, causeException: Throwable) : super(message, causeException)
}
