package net.raboof.kotlisp

interface Expr {
    fun evaluate(environment: ChainedEnvironment): Expr
    fun print(): String
}