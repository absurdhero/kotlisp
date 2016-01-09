package net.raboof.kotlisp

import org.junit.Test
import kotlin.test.assertEquals

class PreambleTest : EvalHarness(CoreEnvironment()){
    @Test fun funSyntax() {
        eval("(fun {minus a b} {- b a})")
        assertEquals("90", eval("(minus 10 100)"))
    }
}