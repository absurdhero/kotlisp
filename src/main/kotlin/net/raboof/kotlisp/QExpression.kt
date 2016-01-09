package net.raboof.kotlisp

data class QExpression(val exprs: List<Expr>) : Expr {
    override fun print(): String {
        return "{${exprs.map { it.print() }.joinToString(" ")}}"
    }

    override fun evaluate(environment: ChainedEnvironment): Expr {
        return this
    }

    override fun toString(): String {
        return print();
    }
}