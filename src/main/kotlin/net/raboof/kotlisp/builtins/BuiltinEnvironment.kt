package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment
import net.raboof.kotlisp.builtins.interop.ctor
import net.raboof.kotlisp.builtins.interop.methodCall

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
        register(isNil)

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

        // strings
        register(concat)
        register(isEmpty)

        // io
        register(load)
        register(printString)
        register(print)
        register(error)
        register(readLine)

        // java interop
        register(ctor)
        register(methodCall)
    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
