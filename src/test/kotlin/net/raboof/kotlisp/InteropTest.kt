package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class InteropTest : EvalHarness() {

    @Test fun ctor() {
        eval("""(def {mystr} (ctor "java.lang.String" "foo"))""")
        assertEquals("\"foo\"", eval("mystr"))
        assertEquals("\"FOO\"", eval("""(. mystr "toUpperCase")"""))
    }

}