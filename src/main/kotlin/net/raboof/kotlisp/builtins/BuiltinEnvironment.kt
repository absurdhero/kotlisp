package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment

class BuiltinEnvironment : ChainedEnvironment() {
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
        register(put)
        register(cons)
        register(len)
    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
