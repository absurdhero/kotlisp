package net.raboof.kotlisp


data class Number(val value: String) : Expr {
    override fun print(): String {
        return value
    }

    override fun evaluate(environment: ChainedEnvironment): Number {
        return this
    }
}