package com.open200.xesar.connect.exception

/** Exception thrown when an HTTP error occurs. */
class HttpErrorException : XesarApiException {

    var httpErrorCode: Int? = null

    constructor(message: String, httpErrorCode: Int) : super(message) {
        this.httpErrorCode = httpErrorCode
    }
}
