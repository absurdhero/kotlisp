package net.raboof.kotlisp

import java.util.*

open class ChainedEnvironment(
        val parent: Environment = Environment.Empty(),
        private val map: MutableMap<String, Expr> = HashMap())
: Environment {

    public override operator fun get(value: String): Expr {
        return map[value] ?: parent[value] ?: throw IllegalArgumentException("unknown symbol $value")
    }

    public override operator fun contains(value: String): Boolean {
        return map.containsKey(value)
    }

    public override operator fun set(key: String, value: Expr) {
        map[key] = value
    }

    public override fun symbols(): List<String> = map.keys.toList() + parent.symbols()

    /** return a new environment with the contents of this environment but with a different parent */
    public fun childOf(parent: Environment): ChainedEnvironment {
        return ChainedEnvironment(parent, HashMap(map))
    }

    public fun global(): ChainedEnvironment {
        return when (parent) {
            is ChainedEnvironment -> parent.global()
            else -> this
        }
    }
}