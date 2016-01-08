package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class ListBuiltinTest : EvalHarness() {
    @Test fun evalListOfSexprs() {
        assertEquals("3", eval("(eval (first {(+ 1 2) (+ 10 10)}))"))
    }

    @Test fun rest() {
        assertEquals("{b c}", eval("(rest {a b c})"))
        assertEquals("{}", eval("(rest {a})"))
    }

    @Test fun list() {
        assertEquals("{1 2 3}", eval("(list 1 2 3)"))
    }
}
