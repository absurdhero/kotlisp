package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*

val def = Builtin("def") { env, denv, rest ->
    val symbols = assertType<QExpression>(rest.first().evaluate(env, denv))
    put(env.global(), rest, symbols)
}

val put = Builtin("=") { env, denv, rest ->
    val symbols = assertType<QExpression>(rest.first().evaluate(env, denv))
    put(env, rest, symbols)
}

private fun put(env: ChainedEnvironment, rest: List<Expr>, symbols: QExpression): QExpression {
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
    return QExpression.Empty
}

val lambda = Builtin("\\") { env, denv, rest ->
    val args = rest.component1() as QExpression
    Lambda(args, rest.component2(), env)
}

val env = Builtin("env") { env, denv, rest ->
    QExpression(env.symbols().sorted().map { Symbol(it) })
}

val dynamicDef = Builtin("dynamic-def") { env, denv, rest ->
    val symbols = assertType<QExpression>(rest.first().evaluate(env, denv))
    put(denv, rest, symbols)
}

val dynamic = Builtin("dynamic") { env, denv, rest ->
    assertLength(rest, 1)
    val symbols = assertType<QExpression>(rest.first().evaluate(env, denv))
    val symbol = assertType<Symbol>(symbols.exprs[0])
    denv[symbol.value]
}

val dynamicEnv = Builtin("dynamic-env") { env, denv, rest ->
    QExpression(denv.symbols().sorted().map { Symbol(it) })
}