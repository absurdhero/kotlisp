package net.raboof.kotlisp

import net.raboof.kotlisp.builtins.Builtin

data class SExpression(val exprs: List<Expr>) : Expr {
    override fun print(): String {
        return "(${exprs.map { it.print() }.joinToString(" ")})"
    }

    override fun toString(): String {
        return print()
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        if (exprs.size == 0) {
            return this
        }

        // first evaluate items from left to right
        val head = exprs.first().evaluate(environment)
        val rest = exprs.subList(1, exprs.size).map { it.evaluate(environment) }

        return when (head) {
            is Builtin -> {
                head.invoke(environment, rest)
            }
            is Lambda -> {
                head.invoke(rest)
            }
            is Number, is Symbol, is True, is False -> {
                if (rest.size > 0) {
                    throw IllegalArgumentException("cannot evaluate ${head.print()} as a function")
                }
                head
            }
            is SExpression -> {
                throw IllegalArgumentException("cannot evaluate s-expression ${head.print()} as a function")
            }
            else -> throw IllegalArgumentException("unknown result of expression type $head")
        }
    }

}