package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class ListBuiltinTest : EvalHarness() {
    @Test fun evalListOfSexprs() {
        assertEquals("3", eval("(eval (first {(+ 1 2) (+ 10 10)}))"))
    }

    @Test fun first() {
        assertEquals("a", eval("(first {a b c})"))
        assertEquals("a", eval("(first {a})"))
    }

    @Test fun head() {
        assertEquals("{a}", eval("(head {a b c})"))
        assertEquals("{a}", eval("(head {a})"))
    }

    @Test fun rest() {
        assertEquals("{b c}", eval("(rest {a b c})"))
        assertEquals("{}", eval("(rest {a})"))
        assertEquals("{}", eval("(rest {})"))
    }

    @Test fun list() {
        assertEquals("{1 2 3}", eval("(list 1 2 3)"))
    }

    @Test fun cons() {
        assertEquals("{1 2 3}", eval("(cons 1 {2 3})"))
        assertEquals("{{1} 2 3}", eval("(cons {1} {2 3})"))
    }

    @Test fun len() {
        assertEquals("2", eval("(len {1 1})"))
    }
}
