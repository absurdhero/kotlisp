package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*
import net.raboof.kotlisp.Number

val plus = mathBuiltin("+", { it.fold (0L, { acc, next -> acc + next }) })
val minus = mathBuiltin("-", { it.reduce { acc, next -> acc - next } })
val multiply = mathBuiltin("*", { it.fold (1L, { acc, next -> acc * next }) })
val divide = mathBuiltin("/", { it.reduce { acc, next -> acc / next } })

fun mathBuiltin(opName: String, f: (List<Long>) -> Long): Builtin {
    return Builtin(opName, { env, rest -> Number(f(numberTerms(env, opName, rest)).toString()) })
}

private fun numberTerms(env: ChainedEnvironment, head: String, rest: List<Expr>): List<Long> {
    return rest.map {
        when (it) {
            is Number -> it.evaluate(env).value.toLong()
            is SExpression -> {
                val value = it.evaluate(env)
                when (value) {
                    is Number -> value.value.toLong()
                    else -> throw IllegalArgumentException("cannot evaluate argument ${it.print()} of $head")
                }
            }
            else -> throw IllegalArgumentException("cannot evaluate argument ${it.print()} of $head")
        }
    }
}

val gt = Builtin(">") { env, rest ->
    val first = assertType<Number>(rest.component1())
    val second = assertType<Number>(rest.component2())

    if (first.toLong() > second.toLong()) True else False
}

val lt = Builtin("<") { env, rest ->
    val first = assertType<Number>(rest.component1())
    val second = assertType<Number>(rest.component2())

    if (first.toLong() < second.toLong()) True else False
}

val gte = Builtin(">=") { env, rest ->
    val first = assertType<Number>(rest.component1())
    val second = assertType<Number>(rest.component2())

    if (first.toLong() >= second.toLong()) True else False
}

val lte = Builtin("<=") { env, rest ->
    val first = assertType<Number>(rest.component1())
    val second = assertType<Number>(rest.component2())

    if (first.toLong() <= second.toLong()) True else False
}
