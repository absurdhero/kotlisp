package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*

val concat = Builtin("concat") { env, denv, rest ->
    Str(rest.fold("") { last, it ->
        when (it) {
            is Str -> last + it.value
            else -> throw IllegalArgumentException("expected string but got ${it.print()}")
        }
    })
}

// unlike nil?, this applies to strings, sexprs, and qexprs
val isEmpty = Builtin("empty?") { env, denv, rest ->
    assertLength(rest, 1)
    when(rest.first()) {
        QExpression.Empty -> True
        QExpression.Empty -> True
        Str("") -> True
        else -> False
    }

}

val symbol = Builtin("symbol") { env, denv, rest ->
    assertLength(rest, 1)
    val first = assertType<Str>(rest.first())
    Symbol(first.value)
}