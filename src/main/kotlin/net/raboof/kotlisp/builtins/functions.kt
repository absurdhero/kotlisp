package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*

val def = Builtin("def") { env, rest ->
    val symbols = assertType<QExpression>(rest.first().evaluate(env))
    put(env.global(), rest, symbols)
}

val put = Builtin("=") { env, rest ->
    val symbols = assertType<QExpression>(rest.first().evaluate(env))
    put(env, rest, symbols)
}

private fun put(env: ChainedEnvironment, rest: List<Expr>, symbols: QExpression): SExpression {
    val values = QExpression(rest.drop(1))

    if (symbols.exprs.size != values.exprs.size) {
        throw IllegalArgumentException("mismatched number of defined symbols and values")
    }

    for (s in symbols.exprs) {
        if (s !is Symbol) {
            throw IllegalArgumentException("cannot define non-symbol")
        }
    }

    for ((s, v) in symbols.exprs.zip(values.exprs)) {
        env[(s as Symbol).value] = v
    }
    return SExpression.Empty
}

val lambda = Builtin("\\") { env, rest ->
    val args = rest.component1() as QExpression
    Lambda(args, rest.component2(), env)
}

val env = Builtin("env") { env, rest ->
    QExpression(env.symbols().sorted().map { Symbol(it) })
}
