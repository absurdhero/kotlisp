package net.raboof.kotlisp

interface Expr {
    fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): Expr
    fun print(): String
}