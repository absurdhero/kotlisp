package net.raboof.kotlisp

data class QExpression(val exprs: List<Expr>) : Expr {
    companion object {
        val Empty = QExpression(emptyList())
    }

    override fun print(): String {
        return "{${exprs.map { it.print() }.joinToString(" ")}}"
    }

    override fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): Expr {
        return this
    }

    override fun toString(): String {
        return print()
    }
}