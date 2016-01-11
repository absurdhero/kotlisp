package net.raboof.kotlisp


data class Number(val value: String) : Expr {
    constructor(l: Long) : this(l.toString()) {
    }

    constructor(i: Int) : this(i.toString()) {
    }

    override fun print(): String {
        return value
    }

    override fun evaluate(environment: ChainedEnvironment): Number {
        return this
    }

    fun toLong(): Long {
        return value.toLong()
    }

    fun toInt(): Int {
        return value.toInt()
    }
}