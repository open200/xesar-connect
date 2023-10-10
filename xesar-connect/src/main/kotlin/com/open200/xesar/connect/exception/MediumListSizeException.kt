package com.open200.xesar.connect.exception

/** Exception indicating that the received list contains more items that expected. */
class MediumListSizeException : XesarApiException {

    constructor() : super()

    constructor(message: String) : super(message)

    constructor(causeException: Throwable) : super(causeException)

    constructor(message: String, causeException: Throwable) : super(message, causeException)
}
