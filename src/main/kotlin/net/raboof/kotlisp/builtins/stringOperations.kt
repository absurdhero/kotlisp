package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*

val concat = Builtin("concat") { env, rest ->
    Str(rest.fold("") { last, it ->
        when (it) {
            is Str -> last + it.value
            else -> throw IllegalArgumentException("expected string but got ${it.print()}")
        }
    })
}

// unlike nil?, this applies to strings, sexprs, and qexprs
val isEmpty = Builtin("empty?") { env, rest ->
    assertLength(rest, 1)
    when(rest.first()) {
        SExpression.Empty -> True
        QExpression.Empty -> True
        Str("") -> True
        else -> False
    }

}