package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment
import net.raboof.kotlisp.Expr

class Builtin(val name: String, val f: (ChainedEnvironment, ChainedEnvironment, List<Expr>) -> Expr) : Expr {
    override fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): Expr {
        return this
    }

    operator fun invoke(env: ChainedEnvironment, denv: ChainedEnvironment, rest: List<Expr>): Expr {
        try {
            return f(env, denv, rest)
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("$name " + e.message, e)
        }
    }

    override fun print(): String {
        return "Builtin<$name>"
    }

    override fun toString(): String {
        return print()
    }
}
