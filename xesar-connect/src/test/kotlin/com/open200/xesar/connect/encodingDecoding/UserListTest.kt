package com.open200.xesar.connect.encodingDecoding

import com.open200.xesar.connect.messages.query.*
import com.open200.xesar.connect.testutils.UserFixture.userFixture
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import java.time.LocalDateTime
import java.util.*

class UserListTest :
    FunSpec({
        val usersList =
            QueryList(
                UUID.fromString("00000000-1281-40ae-89d7-5c541d77a757"),
                QueryList.Response(
                    listOf(
                        userFixture,
                        userFixture.copy(
                            id = UUID.fromString("cf45d8d5-4f99-4d85-a9b3-204c2ed3c56a"),
                            name = "lastname 2 String",
                            lastActive = LocalDateTime.parse("2023-07-07T15:03:00.422502"),
                        ),
                    ),
                    2,
                    2,
                ))

        val usersString =
            "{\"requestId\":\"00000000-1281-40ae-89d7-5c541d77a757\"," +
                "\"response\":{\"data\":[" +
                "{\"id\":\"ffcf5e00-83ad-40cb-b37b-e91abb6f75cd\"," +
                "\"name\":\"lastname String\"," +
                "\"active\":true," +
                "\"description\":\"firstname String\"," +
                "\"lastActive\":\"2023-06-15T16:25:52.225991\"," +
                "\"lastLogin\":\"2023-06-15T16:23:25.229593\"," +
                "\"loginIp\":\"string\"," +
                "\"loginType\":null}," +
                "{\"id\":\"cf45d8d5-4f99-4d85-a9b3-204c2ed3c56a\"," +
                "\"name\":\"lastname 2 String\"," +
                "\"active\":true," +
                "\"description\":\"firstname String\"," +
                "\"lastActive\":\"2023-07-07T15:03:00.422502\"," +
                "\"lastLogin\":\"2023-06-15T16:23:25.229593\"," +
                "\"loginIp\":\"string\"," +
                "\"loginType\":null}]," +
                "\"totalCount\":2,\"filterCount\":2}}"

        test("encoding QueryListElement for a list of users") {
            val usersEncoded = encodeQueryList(usersList)

            usersEncoded.shouldBeEqual(usersString)
        }

        test("decoding QueryListElement for a list of users") {
            val usersDecoded = decodeQueryList<User>(usersString)
            usersDecoded.shouldBe(usersList)
        }
    })
