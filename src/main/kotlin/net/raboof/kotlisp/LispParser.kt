package net.raboof.kotlisp

import net.raboof.parsekt.*
import kotlin.text.Regex
import kotlin.text.isDigit
import kotlin.text.substring


public class LispParser() : CharParsers<String>() {
    override val anyChar: Parser<String, Char>
        get() = Parser { input: String ->
            when (input.length) {
                0 -> null
                1 -> Result(input[0], "")
                else -> Result(input[0], input.substring(1))
            }
        }

    /** evaluate the input or return null */
    fun evaluate(env: Environment, input: String): Expr? {
        return expr(input)?.value?.evaluate(env);
    }

    val number: Parser<String, Expr> = whitespace and charPrefix('-', repeat1(char(Char::isDigit))).string().map { Number(it) as Expr }

    val symbol = whitespace and concat(char(Regex("""[^(){}\d\s]""")), repeat(char(Regex("""[^(){}\s]""")))).string().map { Symbol(it) as Expr }

    val sexprRef: Reference<String, Expr> = Reference()
    val sexpr = sexprRef.get()

    val qexprRef: Reference<String, Expr> = Reference()
    val qexpr = qexprRef.get()

    val expr = number or symbol or sexpr or qexpr

    init {
        sexprRef.set(repeat(expr).between(wsChar('('), wsChar(')')).map { SExpression(it) })
        qexprRef.set(repeat(expr).between(wsChar('{'), wsChar('}')).map { QExpression(it) })
    }
}