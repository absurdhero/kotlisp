package net.raboof.kotlisp

import net.raboof.kotlisp.builtins.BuiltinEnvironment

class CoreEnvironment : ChainedEnvironment(BuiltinEnvironment()) {
    init {
        LispParser().evaluate(this, this.javaClass.getResourceAsStream("/preamble.lisp").reader().readText())
    }
}