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
                when(head.value) {
                    "first" -> builtinFirst(rest)
                    "rest" -> builtinRest(rest)
                    "eval" -> builtinEval(rest)
                    "list" -> builtinList(rest)
                    "join" -> builtinJoin(rest)
                    "+", "-", "*", "/" -> builtinOp(head, rest)
                    else -> throw IllegalArgumentException("unknown symbol")
                }
            }
            is SExpression -> {
                throw IllegalArgumentException("cannot evaluate s-expression ${head.print()} as a function")
            }
            else -> throw IllegalArgumentException("unknown result of expression type $head")
        }
    }

    private fun builtinFirst(rest: List<Expr>) : Expr {
        val first = rest.first()
        when(first) {
            is QExpression -> return first.exprs.first()
            else -> throw IllegalArgumentException("expected q-expression but got $first")
        }
    }

    private fun builtinRest(rest: List<Expr>) = QExpression(rest.subList(1, exprs.size))

    private fun builtinEval(rest: List<Expr>): Expr {
        if (rest.size != 1) throw IllegalArgumentException("expected 1 argument but got ${rest.size}")
        val first = rest.first()
        when(first) {
            is QExpression -> return SExpression(first.exprs).evaluate()
            else -> throw IllegalArgumentException("expected q-expression but got $first")
        }
    }

    private fun builtinList(rest: List<Expr>): Expr = QExpression(rest)

    private fun builtinJoin(rest: List<Expr>): Expr {
        return QExpression(rest.flatMap {
            when (it) {
                is QExpression -> it.exprs
                else -> throw IllegalArgumentException("expected q-expression but got $it")
            }
        })
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