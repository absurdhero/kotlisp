package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*

val trueSymbol = Builtin("#t", {env, rest -> True })
val falseSymbol = Builtin("#f", {env, rest -> False })

val ifCondition = Builtin("if") {env, rest ->
    val predicate = SExpression(assertType<QExpression>(rest[0]).exprs)
    val firstBranch = SExpression(assertType<QExpression>(rest[1]).exprs)
    val secondBranch = SExpression(assertType<QExpression>(rest[2]).exprs)

    val result : Expr = predicate.evaluate(env)

    if (result == True) {
        firstBranch.evaluate(env)
    } else if (result == False) {
        secondBranch.evaluate(env)
    } else {
        throw IllegalArgumentException("expected boolean predicate but got ${result.print()}")
    }
}