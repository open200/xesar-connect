package com.open200.xesar.connect.testutils

import com.open200.xesar.connect.messages.DisengagePeriod
import com.open200.xesar.connect.messages.PersonalLog
import com.open200.xesar.connect.messages.query.Person
import java.util.*

object PersonFixture {

    val personFixture =
        Person(
            id = UUID.fromString("0d92657f-a5dd-489b-a788-4306815c5641"),
            partitionId = UUID.fromString("7ae501f7-173e-43e5-842c-662b44720734"),
            firstName = "firstname String",
            lastName = "lastname String",
            identifier = "1234",
            PersonalLog(
                logMode = PersonalLog.PersonalLogModes.dontSave,
                days = 0,
            ),
            disengagePeriod = DisengagePeriod.SHORT,
            identificationMediaCount = 0,
            outdatedMedia = true,
            externalId = "123",
            external = false,
        )
}
