package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Expr
import net.raboof.kotlisp.QExpression
import net.raboof.kotlisp.SExpression

inline fun <reified T> assertType(obj: Expr) {
    if (obj !is T) {
        throw IllegalArgumentException("expected ${T::class.simpleName} but got ${obj.print()}")
    }
}


val first = Builtin("first") { env, rest ->
    val first = rest.first()
    when (first) {
        is QExpression -> first.exprs.first()
        else -> throw IllegalArgumentException("expected q-expression but got ${first.print()}")
    }
}

val rest = Builtin("rest") { env, rest ->
    val first = rest.first()
    when (first) {
        is QExpression -> QExpression(first.exprs.drop(1))
        else -> throw IllegalArgumentException("expected q-expression but got ${first.print()}")
    }
}

val eval = Builtin("eval") { env, rest ->
    if (rest.size != 1) throw IllegalArgumentException("expected 1 argument but got ${rest.size}")
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
    val second = rest.component2()
    assertType<QExpression>(second)
    QExpression(listOf(rest.first()) + (second as QExpression).exprs)
}

val len = Builtin("len") { env, rest ->
    assertType<QExpression>(rest.first())
    net.raboof.kotlisp.Number((rest.first() as QExpression).exprs.size)
}
