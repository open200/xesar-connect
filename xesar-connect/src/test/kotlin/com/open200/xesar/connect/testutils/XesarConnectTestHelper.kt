package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.*

object XesarConnectTestHelper {
    const val TOKEN =
        "JDJhJDEwJDFSNEljZ2FaRUNXUXBTQ25XN05KbE9qRzFHQ1VjMzkvWTBVcFpZb1M4Vmt0dnJYZ0tJVFBx"
    suspend fun connect(config: Config): XesarConnect {

        val client = XesarMqttClient.connectAsync(config).await()
        val api = XesarConnect(client, config)

        api.subscribeAsync(
                Topics(
                    Topics.Event.loggedIn(config.apiProperties.userId),
                    Topics.Event.UNAUTHORIZED_LOGIN_ATTEMPT,
                    Topics.Event.LOGGED_OUT,
                    Topics.Event.error(config.apiProperties.userId)))
            .await()
        api.token = TOKEN

        return api
    }
}
