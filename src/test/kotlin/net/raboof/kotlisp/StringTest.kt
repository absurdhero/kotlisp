package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class StringTest : EvalHarness() {
    @Test fun parse() {
        assertEquals("\"foo", eval("\"\\\"foo\""))
        assertEquals("", eval("\"\""))
        assertTrue("(eq \"foo\" \"foo\")")
    }

    @Test fun len() {
        assertEquals("3", eval("""(len "abc")"""))
    }

    @Test fun first() {
        assertEquals("a", eval("""(first "abc")"""))
        assertEquals("", eval("""(first "")"""))
    }

    @Test fun rest() {
        assertEquals("bc", eval("""(rest "abc")"""))
        assertEquals("", eval("""(rest "a")"""))
        assertEquals("", eval("""(rest "")"""))
    }

    @Test fun concat() {
        assertEquals("ab", eval("""(concat "a" "b")"""))
        assertEquals("b", eval("""(concat "" "b")"""))
    }

    @Test fun isEmpty() {
        assertEquals("#f", eval("""(empty? "a")"""))
        assertEquals("#t", eval("""(empty? "")"""))
    }

}