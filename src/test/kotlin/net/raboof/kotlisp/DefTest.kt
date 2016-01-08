package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class DefTest : EvalHarness() {

    @Test fun symbolLookedUp_whenEvaluatedAsFunction() {
        eval("(def {foo} +)")
        assertEquals("5", eval("(foo 2 3)"))
    }

    @Test fun symbolLookedUp_whenEvaluatedAsArgument() {
        eval("(def {a} 1)")
        assertEquals("3", eval("(+ a 2)"))
    }

}