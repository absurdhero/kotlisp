package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DynamicTest : EvalHarness() {
    @Test fun dynamicFunction() {
        eval("(dynamic-def {minus} (\\ {a b} {- b a}))")

        assertEquals("90", eval("((dynamic {minus}) 10 100)"))
        assertFailsWith(IllegalArgumentException::class, {
            eval("(minus 10 100)")
        })
    }

    @Test fun dynamicResolution() {
        eval("(let {dynamic-def {foo} \"foobar\"})")
        assertEquals("\"foobar\"", eval("(dynamic {foo})"))
    }
}