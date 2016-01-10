package net.raboof.kotlisp.builtins

import net.raboof.kotlisp.ChainedEnvironment
import net.raboof.kotlisp.builtins.interop.*

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
        register(last)
        register(head)
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
        register(symbol)

        // io
        register(load)
        register(printString)
        register(print)
        register(error)
        register(readLine)

        // java interop
        register(ctor)
        register(methodCall)
        register(fieldGet)
        register(fieldSet)
        register(javaInt)
        register(showMethods)
    }

    fun register(builtin: Builtin) {
        this[builtin.name] = builtin
    }
}
