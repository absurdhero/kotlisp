package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class StringTest : EvalHarness() {
    @Test fun parse() {
        check("\\\"foo", "\"\\\"foo\"")
        check("", "\"\"")
        assertTrue("(eq \"foo\" \"foo\")")
    }

    @Test fun len() {
        assertEquals("3", eval("""(len "abc")"""))
    }

    @Test fun first() {
        check("a", """(first "abc")""")
        check("", """(first "")""")
    }

    @Test fun rest() {
        check("bc", """(rest "abc")""")
        check("", """(rest "a")""")
        check("", """(rest "")""")
    }

    @Test fun concat() {
        check("ab", """(concat "a" "b")""")
        check("b", """(concat "" "b")""")
    }

    @Test fun isEmpty() {
        assertEquals("#f", eval("""(empty? "a")"""))
        assertEquals("#t", eval("""(empty? "")"""))
    }

    // wraps expected result in quotes
    private fun check(expected: String, expr: String) {
        assertEquals("\"$expected\"", eval(expr))
    }
}