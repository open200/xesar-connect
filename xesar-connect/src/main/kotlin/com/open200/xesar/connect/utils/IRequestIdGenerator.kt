package com.open200.xesar.connect.utils

import java.util.UUID

fun interface IRequestIdGenerator {
    fun generateId(): UUID
}
