package net.raboof.kotlisp

class Lambda : Expr {
    val args: List<String>
    val body: SExpression
    val env: ChainedEnvironment

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
        env = ChainedEnvironment()
    }

    constructor(args: List<String>, body: SExpression, env: ChainedEnvironment) {
        this.args = args
        this.body = body
        this.env = env
    }

    override fun print(): String {
        return "(\\ $args $body)"
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        return this
    }

    operator fun invoke(environment: ChainedEnvironment, rest: List<Expr>): Expr {
        if (args.size < rest.size) {
            throw IllegalArgumentException("arity mismatch")
        }

        for ((arg, value) in args zip rest) {
            env[arg] = value
        }

        // if some arguments were not supplied, chop off applied args and return a new partial function
        if (args.size > rest.size) {
            return Lambda(args.subList(rest.size, args.size), body, env)
        }

        return body.evaluate(env.childOf(environment))
    }
}