package net.raboof.kotlisp

data class Symbol(val value: String) : Expr {
    override fun print(): String {
        return value
    }

    override fun evaluate(environment: Environment): Symbol {
        return this
    }
}