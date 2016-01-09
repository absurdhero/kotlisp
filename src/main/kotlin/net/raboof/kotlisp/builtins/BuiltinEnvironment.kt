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

        // lists
        register(first)
        register(rest)
        register(list)
        register(join)
        register(cons)
        register(len)

        // logic
        register(trueSymbol)
        register(falseSymbol)
        register(ifCondition)
        register(eq)
        register(and)
        register(or)
        register(isBoolean)

        // numerical
        register(plus)
        register(minus)
        register(multiply)
        register(divide)
        register(gt)
        register(lt)
        register(gte)
        register(lte)

    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
