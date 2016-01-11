package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

/** test functions defined in preamble.lisp */
class PreambleTest : EvalHarness() {
    @Test fun funSyntax() {
        eval("(fun {minus a b} {- b a})")
        assertEquals("90", eval("(minus 10 100)"))
    }

    @Test fun not() {
        assertEquals("2", eval("(if {not #t} {1} {2})"))
        assertEquals("1", eval("(if {not #f} {1} {2})"))
        assertEquals("2", eval("(if {not (< 5 10)} {1} {2})"))
        assertEquals("1", eval("(if {not (> 5 10)} {1} {2})"))
    }

    @Test fun neq() {
        assertTrue("(neq 2 1)")
        assertFalse("(neq 1 1)")
    }

    @Test fun nil() {
        assertTrue("(nil? nil)")
    }
}