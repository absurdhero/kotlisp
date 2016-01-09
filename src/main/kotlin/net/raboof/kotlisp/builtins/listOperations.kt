package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.QExpression
import net.raboof.kotlisp.SExpression


val first = Builtin("first") { env, rest ->
    assertLength(rest, 1)
    val first = assertType<QExpression>(rest.first())
    first.exprs.first();
}

val rest = Builtin("rest") { env, rest ->
    assertLength(rest, 1)
    val first = assertType<QExpression>(rest.first())
    QExpression(first.exprs.drop(1))
}

val eval = Builtin("eval") { env, rest ->
    assertLength(rest, 1)
    val first = rest.first()
    when (first) {
        is QExpression -> SExpression(first.exprs).evaluate(env)
        else -> first.evaluate(env)
    }
}

val list = Builtin("list") { env, rest -> QExpression(rest) }

val join = Builtin("join") { env, rest ->
    QExpression(rest.flatMap {
        when (it) {
            is QExpression -> it.exprs
            else -> throw IllegalArgumentException("expected q-expression but got ${it.print()}")
        }
    })
}

val cons = Builtin("cons") { env, rest ->
    assertLength(rest, 2)
    val second = assertType<QExpression>(rest.component2())
    QExpression(listOf(rest.first()) + second.exprs)
}

val len = Builtin("len") { env, rest ->
    assertLength(rest, 1)
    val first = assertType<QExpression>(rest.first())
    net.raboof.kotlisp.Number(first.exprs.size)
}
