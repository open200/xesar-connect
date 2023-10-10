package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.messages.query.User
import java.time.LocalDateTime
import java.util.*

object UserFixture {

    val userFixture =
        User(
            id = UUID.fromString("ffcf5e00-83ad-40cb-b37b-e91abb6f75cd"),
            name = "lastname String",
            active = true,
            description = "firstname String",
            lastActive = LocalDateTime.parse("2023-06-15T16:25:52.225991"),
            lastLogin = LocalDateTime.parse("2023-06-15T16:23:25.229593"),
            loginIp = "string",
        )
}
