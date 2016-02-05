package net.raboof.kotlisp

interface Environment {
    operator fun get(value: String): Expr
    operator fun contains(value: String): Boolean
    operator fun set(key: String, value: Expr)
    fun symbols(): List<String>

    object Empty : Environment {
        override fun contains(value: String): Boolean = false

        override fun symbols(): List<String> = emptyList()

        override operator fun get(value: String) =
                throw IllegalArgumentException("unknown symbol $value")

        override operator fun set(key: String, value: Expr) =
                throw UnsupportedOperationException("cannot set variable in empty environment")

        override fun toString(): String {
            return "Empty"
        }
    }
}