package net.raboof.kotlisp.builtins.interop

import net.raboof.kotlisp.ChainedEnvironment
import net.raboof.kotlisp.Expr

data class IObject(val obj: Any?) : Expr {
    override fun evaluate(env: ChainedEnvironment, denv: ChainedEnvironment): Expr {
        return this
    }

    override fun print(): String {
        return "(interop) " + obj.toString()
    }
}