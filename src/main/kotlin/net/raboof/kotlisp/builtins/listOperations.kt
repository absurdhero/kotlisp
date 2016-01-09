package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Expr
import net.raboof.kotlisp.QExpression
import net.raboof.kotlisp.SExpression

inline fun <reified T> assertType(obj: Expr) : T {
    if (obj !is T) {
        throw IllegalArgumentException("expected ${T::class.simpleName} but got ${obj.print()}")
    }
    return obj
}


val first = Builtin("first") { env, rest ->
    val first = assertType<QExpression>(rest.first())
    first.exprs.first();
}

val rest = Builtin("rest") { env, rest ->
    val first = assertType<QExpression>(rest.first())
    QExpression(first.exprs.drop(1))
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
    val second = assertType<QExpression>(rest.component2())
    QExpression(listOf(rest.first()) + second.exprs)
}

val len = Builtin("len") { env, rest ->
    val first = assertType<QExpression>(rest.first())
    net.raboof.kotlisp.Number(first.exprs.size)
}
