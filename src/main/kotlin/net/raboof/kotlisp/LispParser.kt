package net.raboof.kotlisp

import net.raboof.parsekt.*

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
        var lastValue = result?.value?.evaluate(env)

        while (result != null && result.rest.isNotEmpty()) {
            result = line(result.rest)
            lastValue = result?.value?.evaluate(env)
        }

        return lastValue
    }

    val number: Parser<String, Expr> = charPrefix('-', repeat1(char(Char::isDigit))).string().map { Number(it) }
    val string: Parser<String, Expr> = repeat((char('\\') and anyChar) or  char(Regex("[^\"]"))).between(wsChar('"'), char('"')).string().map { Str(it) }

    val symbol: Parser<String, Expr> = concat(char(Regex("""[^(){}\d\s]""")), repeat(char(Regex("""[^(){}\s]""")))).string().map { Symbol(it) }

    val sexprRef: Reference<String, Expr> = Reference()
    val sexpr = sexprRef.get()

    val qexprRef: Reference<String, Expr> = Reference()
    val qexpr = qexprRef.get()

    val expr = whitespace and (number or string or symbol or sexpr or qexpr)

    // skip lines starting with ;
    var newline = char(Regex("[\n\r]"))
    val line = repeat(concat(char(';'), repeat(char(Regex("[^\n\r]")))) and repeat1(newline)) and (expr before repeat(newline))

    init {
        sexprRef.set(repeat(expr).between(wsChar('('), wsChar(')')).map { SExpression(it) })
        qexprRef.set(repeat(expr).between(wsChar('{'), wsChar('}')).map { QExpression(it) })
    }
}