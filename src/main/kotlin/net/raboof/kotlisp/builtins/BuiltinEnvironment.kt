package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Environment

class BuiltinEnvironment : Environment() {
    init {
        register(first)
        register(rest)
        register(list)
        register(join)
        register(eval)
        register(plus)
        register(minus)
        register(multiply)
        register(divide)
        register(def)
    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
