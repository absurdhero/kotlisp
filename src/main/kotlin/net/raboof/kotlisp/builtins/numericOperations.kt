package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.*
import net.raboof.kotlisp.Number

val plus = mathBuiltin("+", { it.fold (0L, { acc, next -> acc + next }) })
val minus = mathBuiltin("-", { it.reduce { acc, next -> acc - next } })
val multiply = mathBuiltin("*", { it.fold (1L, { acc, next -> acc * next }) })
val divide = mathBuiltin("/", { it.reduce { acc, next -> acc / next } })
val modulo = mathBuiltin("%", { it.reduce { acc, next -> acc % next }})

val gt = Builtin(">") { env, denv, rest -> comparison(rest, { a, b -> a > b}) }
val lt = Builtin("<") { env, denv, rest -> comparison(rest, { a, b -> a < b}) }
val gte = Builtin(">=") { env, denv, rest -> comparison(rest, { a, b -> a >= b}) }
val lte = Builtin("<=") { env, denv, rest -> comparison(rest, { a, b -> a <= b}) }

fun mathBuiltin(opName: String, f: (List<Long>) -> Long): Builtin {
    return Builtin(opName, { env, denv, rest -> Number(f(numberTerms(env, denv, opName, rest)).toString()) })
}

private fun numberTerms(env: ChainedEnvironment, denv: ChainedEnvironment, head: String, rest: List<Expr>): List<Long> {
    return rest.map {
        when (it) {
            is Number -> it.evaluate(env, denv).value.toLong()
            else -> throw IllegalArgumentException("cannot evaluate argument ${it.print()} of $head")
        }
    }
}

private fun comparison(args: List<Expr>, comparator: (Long, Long) -> Boolean) : Expr {
    assertLength(args, 2)
    val first = assertType<Number>(args.component1()).toLong()
    val second = assertType<Number>(args.component2()).toLong()

    return if (comparator.invoke(first, second)) True else False
}
