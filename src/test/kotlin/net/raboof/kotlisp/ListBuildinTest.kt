package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class ListBuildinTest : EvalHarness() {
    @Test fun evalListOfSexprs() {
        assertEquals("3", eval("(eval (first {(+ 1 2) (+ 10 10)}))"))
    }

    @Test fun list() {
        assertEquals("{1 2 3}", eval("(list 1 2 3)"))
    }
}
