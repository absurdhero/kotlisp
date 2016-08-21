package net.raboof.kotlisp

import net.raboof.kotlisp.builtins.Builtin

data class SExpression(val exprs: List<Expr>) : Expr {
    companion object {
        val Empty = SExpression(emptyList())
    }

    override fun print(): String {
        return "(${exprs.map { it.print() }.joinToString(" ")})"
    }

    override fun toString(): String {
        return print()
    }

    override fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): Expr {
        if (exprs.size == 0) {
            return QExpression.Empty
        }

        // first evaluate items from left to right
        val head = exprs.first().evaluate(env, denv)
        val rest = exprs.subList(1, exprs.size).map { it.evaluate(env, denv) }

        return when (head) {
            is Builtin -> {
                head.invoke(env, denv, rest)
            }
            is Lambda -> {
                head.invoke(denv, rest)
            }
            is Str, is Number, is Symbol, is True, is False, QExpression.Empty -> {
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