package com.open200.xesar.connect

import com.open200.xesar.connect.messages.query.EventType
import com.open200.xesar.connect.messages.query.GroupOfEvent
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class AccessProtocolEventTopicTest :
    FunSpec({
        test("accessProtocolEventTopic should return correct topic for NORMAL_OPENING event") {
            Topics.Event.accessProtocolEventTopic(EventType.NORMAL_OPENING)
                .shouldBe("xs3/1/ase/MediumEvents/0001")
        }

        test(
            "accessProtocolEventTopic should return correct topic for every access protocol event"
        ) {
            Topics.Event.accessProtocolEventTopic(GroupOfEvent.MediumEvents)
                .shouldBe("xs3/1/ase/MediumEvents/+")
        }
    })
