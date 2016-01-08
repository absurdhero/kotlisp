package net.raboof.kotlisp

class Lambda : Expr {
    val args: List<String>
    val body: SExpression

    constructor(arguments: QExpression, rest: Expr) {
        args = arguments.exprs.map {
            when (it) {
                is Symbol -> it.value
                else -> throw IllegalArgumentException("cannot create function with argument $it")
            }
        }

        when (rest) {
            is QExpression -> body = SExpression(rest.exprs)
            else -> throw IllegalArgumentException("function body must be a q-expression")
        }
    }

    override fun print(): String {
        return "(\\ $args $body)"
    }

    override fun evaluate(environment: Environment): Expr {
        return this
    }

    operator fun invoke(environment: Environment, rest: List<Expr>): Expr {
        if (args.size != rest.size) {
            throw IllegalArgumentException("arity mismatch");
        }

        val fenv = Environment(environment)
        for ((arg, value) in args zip rest) {
            fenv[arg] = value
        }
        return body.evaluate(fenv)
    }
}