package net.raboof.kotlisp

class Lambda : Expr {
    val args: List<String>
    val formalArgs: List<String>
    val vararg: String?
    val body: SExpression
    val enclosingEnv: Environment

    constructor(arguments: QExpression, rest: Expr) : this(
            arguments.exprs.map {
                when (it) {
                    is Symbol -> it.value
                    else -> throw IllegalArgumentException("cannot create function with argument $it")
                }
            },
            when (rest) {
                is QExpression -> SExpression(rest.exprs)
                else -> throw IllegalArgumentException("function body must be a q-expression")
            }, Environment.Empty)

    constructor(args: List<String>, body: SExpression, enclosingEnv: Environment) {
        this.args = args
        this.body = body
        this.enclosingEnv = enclosingEnv

        // find vararg declaration and separate from positional args
        val ampIndex = args.indexOf("&")
        if (ampIndex == -1) {
            formalArgs = args
            vararg = null
        } else {
            formalArgs = args.subList(0, ampIndex)
            val afterAmp = args.subList(ampIndex + 1, args.size)

            if (afterAmp.size == 1) {
                vararg = afterAmp.first()
            } else {
                throw IllegalArgumentException("expected exactly one symbol after '&' vararg symbol but got $afterAmp")
            }
        }
    }

    override fun print(): String {
        return "(\\ $args $body)"
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        return this
    }

    operator fun invoke(parentEnv: Environment, givenArgs: List<Expr>): Expr {
        val env = when(enclosingEnv) {
            is ChainedEnvironment -> ChainedEnvironment(enclosingEnv.childOf(parentEnv))
            else -> ChainedEnvironment(parentEnv)
        }

        for ((arg, value) in args zip givenArgs) {
            env[arg] = value
        }

        // too many arguments passed in?
        if (formalArgs.size < givenArgs.size) {
            if (vararg == null)
                throw IllegalArgumentException("arity mismatch")
            else
                env[vararg] = QExpression(givenArgs.subList(formalArgs.size, givenArgs.size))
        } else if (formalArgs.size > givenArgs.size) {
            // if some arguments were not supplied, chop off applied args and return a new partial function
            return Lambda(args.subList(givenArgs.size, args.size), body, env)
        } else {
            // bind vararg even if it might be an empty list
            if (vararg != null)
                env[vararg] = QExpression(givenArgs.subList(formalArgs.size, givenArgs.size))
        }

        return body.evaluate(env)
    }
}