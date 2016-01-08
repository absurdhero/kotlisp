package net.raboof.kotlisp

data class Symbol(val value: String) : Expr {
    override fun print(): String {
        return value
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        return environment[value]
    }
}