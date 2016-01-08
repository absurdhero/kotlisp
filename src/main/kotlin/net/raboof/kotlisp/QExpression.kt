package net.raboof.kotlisp

import kotlin.collections.*


data class QExpression(val exprs: List<Expr>) : Expr {
    override fun print(): String {
        return "{${exprs.map{ it.print() }.joinToString(" ")}}"
    }

    override fun evaluate(environment: Environment): Expr {
        return this
    }

    override fun toString() : String {
        return print();
    }
}