package net.raboof.kotlisp

import kotlin.collections.*
import kotlin.text.toLong


data class SExpression(val exprs: List<Expr>) : Expr {
    override fun print(): String {
        return "(${exprs.map{ it.print() }.joinToString(" ")})"
    }

    override fun evaluate(): Expr {
        if (exprs.size == 0) {
            return this;
        }

        // first evaluate items from left to right
        val head = exprs.first().evaluate()
        val rest = exprs.subList(1, exprs.size).map { it.evaluate() }

        return when (head) {
            is Number -> {
                if (rest.size > 0) {
                    throw IllegalArgumentException("cannot evaluate ${head.print()} as a function")
                }
                head
            }
            is Symbol -> {
                builtinOp(head, rest)
            }
            is SExpression -> {
                throw IllegalArgumentException("cannot evaluate s-expression ${head.print()} as a function")
            }
            else -> throw IllegalArgumentException("unknown result of expression type $head")
        }
    }

    private fun builtinOp(head: Symbol, rest: List<Expr>): Number {
        return Number(when (head.value) {
            "+" -> numberTerms(head, rest).fold (0L, { acc, next -> acc + next })
            "-" -> numberTerms(head, rest).reduce { acc, next -> acc - next }
            "*" -> numberTerms(head, rest).fold (1L, { acc, next -> acc * next })
            "/" -> numberTerms(head, rest).reduce { acc, next -> acc / next }
            else -> throw IllegalArgumentException("unknown operator ${head.print()}")
        }.toString())
    }

    private fun numberTerms(head: Symbol, rest: List<Expr>): List<Long> {
        return rest.map {
            when (it) {
                is Number -> it.evaluate().value.toLong()
                is SExpression -> {
                    val value = it.evaluate()
                    when (value) {
                        is Number -> value.value.toLong()
                        else -> throw IllegalArgumentException("cannot evaluate argument ${it.print()} of ${head.print()}")
                    }
                }
                else -> throw IllegalArgumentException("cannot evaluate argument ${it.print()} of ${head.print()}")
            }
        }
    }
}