package net.raboof.kotlisp

data class Str(val value: String) : Expr {
    override fun print(): String {
        return value
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        return this
    }
}