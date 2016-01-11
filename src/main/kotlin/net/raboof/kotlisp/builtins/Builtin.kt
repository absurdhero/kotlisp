package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment
import net.raboof.kotlisp.Expr

class Builtin(val name: String, val f: (ChainedEnvironment, List<Expr>) -> Expr) : Expr {
    override fun evaluate(environment: ChainedEnvironment): Expr {
        return this
    }

    operator fun invoke(environment: ChainedEnvironment, rest: List<Expr>): Expr {
        try {
            return f(environment, rest)
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
