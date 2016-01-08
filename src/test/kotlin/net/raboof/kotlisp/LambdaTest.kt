package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LambdaTest : EvalHarness() {

    @Test fun oneArgument() {
        eval("(def {plusone} (\\ {a} {+ a 1}))")
        assertEquals("3", eval("(plusone 2)"))
    }

    @Test fun checkArity() {
        eval("(def {plusone} (\\ {a} {+ a 1}))")

        assertFailsWith(IllegalArgumentException::class, {
            eval("(plusone 2 3)")
        })
    }

}