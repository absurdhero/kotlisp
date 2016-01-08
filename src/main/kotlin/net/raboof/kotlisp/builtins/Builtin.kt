package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment
import net.raboof.kotlisp.Expr

class Builtin(val name: String, val f: (ChainedEnvironment, List<Expr>) -> Expr) : Expr {
    override fun evaluate(environment: ChainedEnvironment): Expr {
        throw UnsupportedOperationException()
    }

    operator fun invoke(environment: ChainedEnvironment, rest: List<Expr>): Expr {
        return f(environment, rest)
    }

    override fun print(): String {
        return "Builtin<$name>"
    }
}
