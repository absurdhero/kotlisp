package net.raboof.kotlisp

import net.raboof.kotlisp.builtins.BuiltinEnvironment
import kotlin.test.assertEquals

abstract class EvalHarness(val env: ChainedEnvironment = BuiltinEnvironment()) {
    val parser = LispParser()

    protected fun eval(expression: String): String? = parser.evaluate(env, expression)?.print()

    fun assertTrue(expr: String) {
        assertEquals("#t", eval(expr))
    }

    fun assertFalse(expr: String) {
        assertEquals("#f", eval(expr))
    }
}
