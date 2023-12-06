package com.open200.xesar.connect.exception

/** Exception thrown when an HTTP error occurs. */
class HttpErrorException : XesarApiException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(causeException: Throwable) : super(causeException)

    constructor(message: String, causeException: Throwable) : super(message, causeException)
}
