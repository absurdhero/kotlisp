package net.raboof.kotlisp

interface Expr {
    fun evaluate(): Expr
    fun print(): String
}