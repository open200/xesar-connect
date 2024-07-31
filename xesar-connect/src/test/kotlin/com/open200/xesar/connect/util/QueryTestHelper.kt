package com.open200.xesar.connect.util

import com.open200.xesar.connect.it.MosquittoContainer
import java.util.*

object QueryTestHelper {

    fun createQueryRequest(resourceName: String, requestId: UUID, queryObjectId: UUID): String {
        return "{\"resource\":\"${resourceName}\",\"requestId\":\"${requestId}\",\"token\":\"${MosquittoContainer.TOKEN}\",\"id\":\"${queryObjectId}\",\"params\":null}"
    }

    fun createQueryRequest(resourceName: String, requestId: UUID): String {
        return "{\"resource\":\"${resourceName}\",\"requestId\":\"${requestId}\",\"token\":\"${MosquittoContainer.TOKEN}\",\"id\":null,\"params\":null}"
    }
}
