package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.QExpression
import net.raboof.kotlisp.SExpression
import kotlin.collections.first
import kotlin.collections.flatMap


val first = Builtin("first", {env, rest ->
    val first = rest.first()
    when(first) {
        is QExpression -> first.exprs.first()
        else -> throw IllegalArgumentException("expected q-expression but got ${first.print()}")
    }
})

val rest = Builtin("rest", {env, rest -> QExpression(rest.subList(1, rest.size))})

val eval = Builtin("eval", {env, rest ->
    if (rest.size != 1) throw IllegalArgumentException("expected 1 argument but got ${rest.size}")
    val first = rest.first()
    when(first) {
        is QExpression -> SExpression(first.exprs).evaluate(env)
        else -> first.evaluate(env)
    }
})

val list = Builtin("list", {env, rest -> QExpression(rest)})

val join = Builtin("join", { env, rest ->
    QExpression(rest.flatMap {
        when (it) {
            is QExpression -> it.exprs
            else -> throw IllegalArgumentException("expected q-expression but got ${it.print()}")
        }
    })
})