package net.raboof.kotlisp

import net.raboof.kotlisp.builtins.Builtin
import kotlin.collections.first
import kotlin.collections.joinToString
import kotlin.collections.map


data class SExpression(val exprs: List<Expr>) : Expr {
    override fun print(): String {
        return "(${exprs.map{ it.print() }.joinToString(" ")})"
    }

    override fun evaluate(environment: Environment): Expr {
        if (exprs.size == 0) {
            return this;
        }

        // first evaluate items from left to right
        val head = exprs.first().evaluate(environment)
        val rest = exprs.subList(1, exprs.size).map { it.evaluate(environment) }

        return when (head) {
            is Number -> {
                if (rest.size > 0) {
                    throw IllegalArgumentException("cannot evaluate ${head.print()} as a function")
                }
                head
            }
            is Symbol -> {
                val value = environment[head.value]
                when(value) {
                    is Builtin -> value.evaluate(environment, rest)
                    null -> throw IllegalArgumentException("unknown symbol $head")
                    else -> throw IllegalArgumentException("cannot evaluate ${value.print()} as a function")
                }
            }
            is SExpression -> {
                throw IllegalArgumentException("cannot evaluate s-expression ${head.print()} as a function")
            }
            else -> throw IllegalArgumentException("unknown result of expression type $head")
        }
    }

}