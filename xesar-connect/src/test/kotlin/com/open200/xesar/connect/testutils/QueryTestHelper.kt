package com.open200.xesar.connect.testutils

import java.util.*

object QueryTestHelper {

    fun createQueryRequest(resourceName: String, requestId: UUID, queryObjectId: UUID): String {
        return "{\"resource\":\"${resourceName}\",\"requestId\":\"${requestId}\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":\"${queryObjectId}\",\"params\":null}"
    }

    fun createQueryRequest(resourceName: String, requestId: UUID): String {
        return "{\"resource\":\"${resourceName}\",\"requestId\":\"${requestId}\",\"token\":\"${XesarConnectTestHelper.TOKEN}\",\"id\":null,\"params\":null}"
    }
}
