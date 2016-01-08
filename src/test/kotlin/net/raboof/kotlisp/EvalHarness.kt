package net.raboof.kotlisp

import net.raboof.kotlisp.builtins.BuiltinEnvironment

abstract class EvalHarness {
    var env : ChainedEnvironment = BuiltinEnvironment()
    val parser = LispParser()

    protected fun eval(expression: String): String? = parser.evaluate(env, expression)?.print()
}
