package com.open200.xesar.connect.testutils

import io.kotest.core.annotation.EnabledCondition
import io.kotest.core.spec.Spec
import kotlin.reflect.KClass

class EnableTestLocally : EnabledCondition {

    override fun enabled(kclass: KClass<out Spec>): Boolean =
        when {
            System.getenv("CI")?.toBoolean() != true -> true
            else -> false
        }
}
