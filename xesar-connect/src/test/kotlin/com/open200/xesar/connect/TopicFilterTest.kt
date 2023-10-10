package com.open200.xesar.connect

import com.open200.xesar.connect.filters.TopicFilter
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeFalse
import io.kotest.matchers.booleans.shouldBeTrue

class TopicFilterTest :
    FunSpec({
        val topic = listOf("topic", "session")

        test("should return true when topic is valid") {
            TopicFilter(topic).filter("topic", "{\"commandId\":\"$topic\"}").shouldBeTrue()
        }

        test("should return false when topic is invalid") {
            TopicFilter(topic).filter("query", "{\"comamndId\":\"$topic\"}").shouldBeFalse()
        }
    })
