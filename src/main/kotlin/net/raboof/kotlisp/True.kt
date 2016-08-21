package net.raboof.kotlisp

object True : Expr {
    override fun print(): String {
        return "#t"
    }

    override fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): True {
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is True) {
            return true
        }
        return false
    }
}