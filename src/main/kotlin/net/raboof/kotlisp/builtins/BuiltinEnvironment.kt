package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment

class BuiltinEnvironment : ChainedEnvironment() {
    init {
        // environmental
        register(def)
        register(put)
        register(env)
        register(lambda)
        register(eval)

        // numerical
        register(plus)
        register(minus)
        register(multiply)
        register(divide)

        // lists
        register(first)
        register(rest)
        register(list)
        register(join)
        register(cons)
        register(len)
    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
