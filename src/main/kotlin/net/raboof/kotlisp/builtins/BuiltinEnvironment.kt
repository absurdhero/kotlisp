package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.Environment

class BuiltinEnvironment : Environment(null) {
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
        register(env)
        register(lambda)
    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
