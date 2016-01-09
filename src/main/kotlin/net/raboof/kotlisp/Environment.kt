package net.raboof.kotlisp

interface Environment {
    public operator fun get(value: String): Expr
    public operator fun contains(value: String): Boolean
    public operator fun set(key: String, value: Expr)
    public fun symbols(): List<String>

    object Empty : Environment {
        override fun contains(value: String): Boolean = false

        override fun symbols(): List<String> = emptyList()

        public override operator fun get(value: String) =
                throw IllegalArgumentException("unknown symbol $value")

        public override operator fun set(key: String, value: Expr) =
                throw UnsupportedOperationException("cannot set variable in empty environment")

        public override fun toString(): String {
            return "Empty";
        }
    }
}