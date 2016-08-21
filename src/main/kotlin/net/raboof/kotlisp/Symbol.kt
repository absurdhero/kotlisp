package net.raboof.kotlisp

data class Symbol(val value: String) : Expr {
    override fun print(): String {
        return value
    }

    override fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): Expr {
        return env[value]
    }
}