package net.raboof.kotlisp

import net.raboof.parsekt.*

class LispParser() : CharParsers<String>() {
    override val anyChar: Parser<String, Char>
        get() = Parser { input: String ->
            when (input.length) {
                0 -> Result.ParseError<String, Char>("EOF", null, "")
                1 -> Result.Value(input[0], "")
                else -> Result.Value(input[0], input.substring(1))
            }
        }

    /** evaluate the input or throw an exception */
    fun evaluate(env: ChainedEnvironment, input: String): Expr? {
        var result: Result<String, Expr> = statementOrNil(input)
        var lastValue : Expr? = result.valueOrFail().evaluate(env)

        while (result !is Result.ParseError && (result as Result.Value).rest.isNotEmpty()) {
            result = statementOrNil(result.rest)
            lastValue = result.valueOrFail().evaluate(env)
        }

        return lastValue
    }

    val number: Parser<String, Expr> = charPrefix('-', repeat1(char(Char::isDigit))).string().map { Number(it) }
    val string: Parser<String, Expr> = substring(Regex(""""(\\.|[^\\"])*"""")).string().map { Str(Str.unescaped(it.substring(1, it.length-1))) }

    val symbol: Parser<String, Expr> = concat(char(Regex("""[^(){}\d\s]""")), repeat(char(Regex("""[^(){}\s]""")))).string().map { Symbol(it) }

    val sexprRef: Reference<String, Expr> = Reference()
    val sexpr = sexprRef.get()

    val qexprRef: Reference<String, Expr> = Reference()
    val qexpr = qexprRef.get()

    val expr = whitespace and (number or string or symbol or sexpr or qexpr)

    var newline = char(Regex("[\n\r]"))
    // skip lines starting with ;
    var comments = repeat(concat(char(';'), repeat(char(Regex("[^\n\r]")))) and repeat1(newline))

    val statement = comments and (expr before repeat(newline)) before comments
    val statementOrNil = statement or (whitespace.map { QExpression.Empty }).cast<Expr>()

    init {
        sexprRef.set(repeat(expr).between(wsChar('('), wsChar(')')).map { SExpression(it) })
        qexprRef.set(repeat(expr).between(wsChar('{'), wsChar('}')).map { QExpression(it) })
    }
}