package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*

val trueSymbol = Builtin("#t", { env, denv, rest -> True })
val falseSymbol = Builtin("#f", { env, denv, rest -> False })

val ifCondition = Builtin("if") { env, denv, rest ->
    assertLength(rest, 3)
    val predicate = SExpression(assertType<QExpression>(rest[0]).exprs)
    val firstBranch = SExpression(assertType<QExpression>(rest[1]).exprs)
    val secondBranch = SExpression(assertType<QExpression>(rest[2]).exprs)

    val result: Expr = predicate.evaluate(env, denv)

    if (result == True) {
        firstBranch.evaluate(env, denv)
    } else if (result == False) {
        secondBranch.evaluate(env, denv)
    } else {
        throw IllegalArgumentException("expected boolean predicate but got ${result.print()}")
    }
}

val whileLoop = Builtin("while") { env, denv, rest ->
    assertLength(rest, 2)
    val predicate = SExpression(assertType<QExpression>(rest[0]).exprs)
    val body = SExpression(assertType<QExpression>(rest[1]).exprs)

    var lastExpr : Expr = QExpression.Empty
    while (predicate.evaluate(env, denv) == True) {
        lastExpr = body.evaluate(env, denv)
    }
    lastExpr
}

val eq = Builtin("eq") { env, denv, rest ->
    assertLength(rest, 2)
    if (rest[0] == rest[1]) True else False
}

fun assertBoolean(exprs: List<Expr>) {
    for(expr in exprs) {
        if (expr != True && expr != False)
            throw IllegalArgumentException("expected boolean value but got $expr")
    }
}

fun isBool(expr: Expr) : Boolean {
    if (expr == True || expr == False)
        return true
    return false
}

val and = Builtin("and") { env, denv, rest ->
    assertBoolean(rest)
    if (rest.all { term ->
        term == True }) True else False
}
val or = Builtin("or") { env, denv, rest ->
    assertBoolean(rest)
    if (rest.any { term -> term == True }) True else False
}


val isBoolean = Builtin("boolean?") { env, denv, rest -> if (rest.all(::isBool)) True else False }

