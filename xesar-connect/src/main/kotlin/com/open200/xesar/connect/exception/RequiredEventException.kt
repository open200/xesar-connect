package com.open200.xesar.connect.exception

class RequiredEventException(str: String, e: Throwable) : XesarApiException(str, e) {}
