package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Environment
import net.raboof.kotlisp.Expr
import net.raboof.kotlisp.Number
import net.raboof.kotlisp.SExpression
import kotlin.collections.fold
import kotlin.collections.map
import kotlin.collections.reduce
import kotlin.text.toLong


val plus  = mathBuiltin("+", { it.fold (0L, { acc, next -> acc + next })})
val minus = mathBuiltin("-", { it.reduce { acc, next -> acc - next }})
val multiply = mathBuiltin("*", { it.fold (1L, { acc, next -> acc * next })})
val divide = mathBuiltin("/", { it.reduce { acc, next -> acc / next }})

fun mathBuiltin(opName: String, f: (List<Long>) -> Long) : Builtin {
    return Builtin(opName, { env, rest -> Number(f(numberTerms(env, opName, rest)).toString()) })
}

private fun numberTerms(env: Environment, head: String, rest: List<Expr>): List<Long> {
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