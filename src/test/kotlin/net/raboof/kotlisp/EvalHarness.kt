package net.raboof.kotlisp

import kotlin.test.assertEquals

abstract class EvalHarness {
    val env: ChainedEnvironment = CoreEnvironment()
    val denv: ChainedEnvironment = ChainedEnvironment()

    constructor() {
        LispParser().evaluate(env, denv, this.javaClass.getResourceAsStream("/preamble.lisp").reader().readText())
    }


    val parser = LispParser()

    protected fun eval(expression: String): String? = parser.evaluate(env, denv, expression)?.print()

    fun assertTrue(expr: String) {
        assertEquals("#t", eval(expr))
    }

    fun assertFalse(expr: String) {
        assertEquals("#f", eval(expr))
    }
}
