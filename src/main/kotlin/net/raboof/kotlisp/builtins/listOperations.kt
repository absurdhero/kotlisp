package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Expr
import net.raboof.kotlisp.QExpression
import net.raboof.kotlisp.SExpression
import net.raboof.kotlisp.Str

val expectSequence = { arg: Expr -> IllegalArgumentException("expected sequence but got ${arg.print()}") }

val first = Builtin("first") { env, rest ->
    assertLength(rest, 1)
    val arg = rest.first();
    when(arg) {
        is QExpression -> arg.exprs.first()
        is Str -> if(arg.value.length > 0) Str(arg.value[0].toString()) else Str("")
        else -> {
            throw expectSequence(arg)
        }
    }
}

val rest = Builtin("rest") { env, rest ->
    assertLength(rest, 1)
    val arg = rest.first()
    when(arg) {
        is QExpression -> QExpression(arg.exprs.drop(1))
        is Str -> if(arg.value.length > 0) Str(arg.value.substring(1)) else Str("")
        else -> {
            throw expectSequence(arg)
        }
    }
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
    val arg = rest.first()
    when(arg) {
        is QExpression -> net.raboof.kotlisp.Number(arg.exprs.size)
        is Str -> net.raboof.kotlisp.Number(arg.value.length)
        else -> {
            throw expectSequence(arg)
        }
    }
}
