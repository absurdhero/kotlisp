package net.raboof.kotlisp

object False : Expr {
    override fun print(): String {
        return "#f"
    }

    override fun evaluate(environment: ChainedEnvironment): False {
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (other is False) {
            return true
        }
        return false
    }
}