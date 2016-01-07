package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Environment
import net.raboof.kotlisp.Expr

class Builtin(val name: String, val f: (Environment, List<Expr>) -> Expr) : Expr {
    override fun evaluate(environment: Environment): Expr {
        throw UnsupportedOperationException()
    }

    fun evaluate(environment: Environment, rest: List<Expr>): Expr {
        return f(environment, rest)
    }

    override fun print(): String {
        return "#$name#"
    }
}
