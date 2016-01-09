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
    fun evaluate(env: ChainedEnvironment, input: String): Expr? {
        var result: Result<String, Expr>? = line(input)
        var lastValue = result?.value?.evaluate(env);

        while (result != null && result.rest.isNotEmpty()) {
            result = line(result.rest)
            lastValue = result?.value?.evaluate(env)
        }

        return lastValue;
    }

    val number: Parser<String, Expr> = whitespace and charPrefix('-', repeat1(char(Char::isDigit))).string().map { Number(it) as Expr }

    val symbol = whitespace and concat(char(Regex("""[^(){}\d\s]""")), repeat(char(Regex("""[^(){}\s]""")))).string().map { Symbol(it) as Expr }

    val sexprRef: Reference<String, Expr> = Reference()
    val sexpr = sexprRef.get()

    val qexprRef: Reference<String, Expr> = Reference()
    val qexpr = qexprRef.get()

    val expr = number or symbol or sexpr or qexpr

    // skip lines starting with ;
    val line = repeat(concat(char(';'), repeat(char(Regex("[^\n\r]")))) and repeat1(char(Regex("[\n\r]")))) and expr

    init {
        sexprRef.set(repeat(expr).between(wsChar('('), wsChar(')')).map { SExpression(it) })
        qexprRef.set(repeat(expr).between(wsChar('{'), wsChar('}')).map { QExpression(it) })
    }
}