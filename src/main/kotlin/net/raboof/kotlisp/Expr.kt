package net.raboof.kotlisp

interface Expr {
    fun evaluate(environment: Environment): Expr
    fun print(): String
}